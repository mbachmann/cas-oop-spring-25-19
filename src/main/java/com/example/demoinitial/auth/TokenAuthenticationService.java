package com.example.demoinitial.auth;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationService {

    public enum AuthHeaderName {
        xauth("X-AUTH-TOKEN"),
        bearer("Authorization");

        private final String headerName;

        AuthHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getHeaderName() {
            return headerName;
        }
    }

    private final AuthHeaderName authHeaderName;
    private final String bearer = "Bearer ";
    private TokenHandler tokenHandler = null;

    public TokenAuthenticationService(AuthHeaderName authHeaderName, String secret, UserDetailsService userDetailsService) {
        tokenHandler = new TokenHandler(secret, userDetailsService);
        this.authHeaderName = authHeaderName;
    }

    public String addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final UserDetails userDetails = authentication.getDetails();
        String token = tokenHandler.createTokenForUser(userDetails);
        if (authHeaderName == AuthHeaderName.bearer) token = bearer + token;
            response.addHeader(authHeaderName.getHeaderName(), token);
        return token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(authHeaderName.getHeaderName());
        if (token != null) {
            if (authHeaderName == AuthHeaderName.bearer) token = token.substring(bearer.length()).trim();
            final UserDetails userDetails = tokenHandler.parseUserFromToken(token);
            if (userDetails != null) {
                return new UserAuthentication(userDetails);
            }
        }
        return null;
    }
}
