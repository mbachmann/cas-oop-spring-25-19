package com.example.demoinitial.auth;

import com.example.demoinitial.AbstractTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenAuthenticationServiceTest extends AbstractTest {

    private TokenAuthenticationService tokenAuthenticationService ;

    @Qualifier("appUserDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    @Value("${hellorest.auth.secret:secretkey}") String secretKey;


    @BeforeEach
    public void setup() throws Exception {
        super.setUp();
    }

    @Test
    public void addXAuthTokenToResponseHeader() {

        tokenAuthenticationService = new TokenAuthenticationService(TokenAuthenticationService.AuthHeaderName.xauth, secretKey, userDetailsService);

        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername("admin@example.com");
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        MockHttpServletResponse response = new MockHttpServletResponse();
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        String token = response.getHeader(TokenAuthenticationService.AuthHeaderName.xauth.getHeaderName());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TokenAuthenticationService.AuthHeaderName.xauth.getHeaderName(), token);
        UserAuthentication userRestoredAuthentication  = (UserAuthentication)tokenAuthenticationService.getAuthentication(request);

        assertEquals(userRestoredAuthentication.getPrincipal(),userRestoredAuthentication.getPrincipal());
    }

    @Test
    public void addBearerTokenToResponseHeader() {

        tokenAuthenticationService = new TokenAuthenticationService(TokenAuthenticationService.AuthHeaderName.bearer, secretKey, userDetailsService);

        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername("admin@example.com");
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        MockHttpServletResponse response = new MockHttpServletResponse();
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        String token = response.getHeader(HttpHeaders.AUTHORIZATION);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, token);
        UserAuthentication userRestoredAuthentication  = (UserAuthentication)tokenAuthenticationService.getAuthentication(request);

        assertEquals(userRestoredAuthentication.getPrincipal(),userRestoredAuthentication.getPrincipal());
    }

}
