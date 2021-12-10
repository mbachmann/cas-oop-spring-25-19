package com.example.demoinitial.web.rest;


import com.example.demoinitial.web.dto.LoginDto;
import com.example.demoinitial.auth.TokenAuthenticationService;
import com.example.demoinitial.auth.UserAuthentication;
import com.example.demoinitial.auth.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class JwtAuthenticationController {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final AuthenticationManager authenticationManagerBean;

    public JwtAuthenticationController(@Qualifier("appUserDetailsServiceImpl") UserDetailsService userDetailsService,
                                       @Value("${hellorest.auth.headername:xauth}") TokenAuthenticationService.AuthHeaderName  authHeaderName,
                                       @Value("${hellorest.auth.secret:secretkey}") String secretKey,
                                       AuthenticationManager authenticationManagerBean) {

        this.tokenAuthenticationService = new TokenAuthenticationService(authHeaderName, secretKey, userDetailsService);
        this.authenticationManagerBean = authenticationManagerBean;
    }

    @PostMapping("/authenticate")
    public JWTToken authorize(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        );
        final Authentication authentication = authenticationManagerBean.authenticate(authenticationToken);
        final UserDetailsImpl authenticatedUserDetails = (UserDetailsImpl)authentication.getPrincipal();
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUserDetails);

        // add the JWT token to the response header
        final String jwt = tokenAuthenticationService.addAuthentication(response, userAuthentication);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
        System.out.println("successful authentication");
        return new JWTToken(jwt);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
