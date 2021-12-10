package com.example.demoinitial.auth;

import com.example.demoinitial.AbstractTest;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TokenHandlerTest extends AbstractTest {

    @Qualifier("appUserDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    private TokenHandler tokenHandler;

    @Value("${hellorest.auth.secret:secretkey}") String secretKey;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenHandler = new TokenHandler(secretKey, userDetailsService);
    }

    @Test
    public void verifyToken() {

        final UserDetails userDetails = userDetailsService.loadUserByUsername("admin@example.com");
        String token = tokenHandler.createTokenForUser(userDetails);

        final UserDetails restoredUserDetails= tokenHandler.parseUserFromToken(token);
        assertEquals(restoredUserDetails.getUsername(), userDetails.getUsername());

        final Claims claims= tokenHandler.parseClaimsFromToken(token);
        assertEquals(claims.getSubject(), userDetails.getUsername());

        List<Map<String, String>> authorities = claims.get("roles", List.class);
        assertTrue(authorities.get(0).containsValue("ROLE_ADMIN"));
    }

}
