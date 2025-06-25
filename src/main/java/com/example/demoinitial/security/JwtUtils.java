package com.example.demoinitial.security;

import com.example.demoinitial.service.UserDetailsImpl;
import com.example.demoinitial.utils.HasLogger;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils implements HasLogger {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${app.jwtCookieName}")
    private String jwtCookieName;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal);
        return ResponseCookie.from(jwtCookieName, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null).path("/api").build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            getLogger().error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            getLogger().error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            getLogger().error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            getLogger().error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(UserDetailsImpl userDetails) {
        SecretKey key = Jwts.SIG.HS512.key().build();
        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(userDetails.getUsername())
            .claim("roles", userDetails.getAuthorities())
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}