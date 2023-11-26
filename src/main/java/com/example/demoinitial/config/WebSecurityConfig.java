package com.example.demoinitial.config;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.example.demoinitial.security.AuthEntryPointJwt;
import com.example.demoinitial.security.AuthTokenFilter;
import com.example.demoinitial.security.JwtUtils;
import com.example.demoinitial.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    private final JwtUtils jwtUtils;

    @Autowired
    public WebSecurityConfig (UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        String[] permittedResources = new String[] {
            "/", "/static/**","/css/**","/js/**","/webfonts/**", "/webjars/**",
            "/index.html","/favicon.ico", "/error",
            "/v3/**","/swagger-ui.html","/swagger-ui/**", "/actuator/**"
        };
        http
            .headers().frameOptions().disable().and()
            .csrf(AbstractHttpConfigurer::disable)
            .securityMatcher(permittedResources)
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
            .requestCache(RequestCacheConfigurer::disable)
            .securityContext(SecurityContextConfigurer::disable)
            .sessionManagement(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http
            //.cors().and()  // uncomment this line with CorsConfigurationSource, comment this line with CorsFilter
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
            .csrf(AbstractHttpConfigurer::disable)
            .securityMatcher("/api/**")
            .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(OPTIONS).permitAll()
                .requestMatchers(antMatcher("/api/auth/**")).permitAll()
                .requestMatchers(antMatcher("/api/test/**")).permitAll()
                .requestMatchers(antMatcher("/api/persons/**")).hasAnyRole("USER", "MODERATOR", "ADMIN")
                .requestMatchers(antMatcher(HttpMethod.GET, "/actuator/**")).permitAll()
                .requestMatchers(
                    antMatcher( "/h2-console/**")).permitAll()
            ).authenticationProvider(authenticationProvider())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    @Order(2)
    protected SecurityFilterChain mvcFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> {

                    requests
                        .requestMatchers(OPTIONS).permitAll()
                        .requestMatchers(antMatcher("/users/**")).hasAnyRole("USER", "MODERATOR", "ADMIN")
                        .requestMatchers(antMatcher("/stomp-broadcast/**")).hasAnyRole("USER", "MODERATOR", "ADMIN")
                        .requestMatchers(antMatcher("/broadcast/**")).hasAnyRole("USER", "MODERATOR", "ADMIN")
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .anyRequest()
                        .authenticated();
                }
            );

        http
            .formLogin(login -> login.loginPage("/login").permitAll())
            .httpBasic(withDefaults())
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * webSecurityCustomizer does not use the filter chain. Spring Boot produces a warning:
     * You are asking Spring Security to ignore Ant [pattern='/js/**', GET].
     * This is not recommended -- please use
     * permitAll via HttpSecurity#authorizeHttpRequests instead.
     *
     * @return
     */
    /* @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(antMatcher(HttpMethod.GET, ("/js/**")))
            .requestMatchers(antMatcher(HttpMethod.GET, ("/css/**")))
            .requestMatchers(antMatcher(HttpMethod.GET, ("/webfonts/**")));
    }*/
}
