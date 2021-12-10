package com.example.demoinitial.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class TokenHandler {

    private final String secret;
    private final UserDetailsService userDetailsService;

    public TokenHandler(String secret, UserDetailsService userDetailsService) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.userDetailsService = userDetailsService;
     }

    public UserDetails parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        System.out.println(username);
        return userDetailsService.loadUserByUsername(username);
     }

    public Claims parseClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();

    }

    public String createTokenForUser(UserDetails userDetails) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());
        Date expiration = new Date(now.getTime() + TimeUnit.HOURS.toMillis(10l));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
