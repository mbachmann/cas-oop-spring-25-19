package com.example.demoinitial.config;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        String[] permitted = new String[]{
            "/", "/static/**","/css/**","/js/**","/webfonts/**", "/webjars/**",
            "/index.html","/favicon.ico", "/error"
        };
        http
            //.headers().frameOptions().disable().and()
            //.csrf().disable()
            //.csrf(AbstractHttpConfigurer::disable)
            .securityMatcher(permitted)
            /*.securityMatcher("/static/**")
            .securityMatcher("/css/**")
            .securityMatcher("/js/**")
            .securityMatcher("/webfonts/**")
            .securityMatcher("/index.html")
            .securityMatcher("/favicon.ico")*/
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
            .requestCache().disable()
            .securityContext().disable()
            .sessionManagement().disable();

        return http.build();
    }

    @Bean
    @Order(1)
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers().frameOptions().disable().and()
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> {

                    requests
                        .requestMatchers(OPTIONS).permitAll()
                        //.requestMatchers(antMatcher(HttpMethod.GET, "/")).permitAll()
                        .requestMatchers(antMatcher("/users/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.POST, "/users/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.PUT, "/users/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.DELETE, "/users/**")).hasAnyRole("USER", "ADMIN")

                        .requestMatchers(antMatcher("/stomp-broadcast/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher("/broadcast/**")).hasAnyRole("USER", "ADMIN")

                        .requestMatchers(antMatcher("/api/persons/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.POST, "/api/persons/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.PUT, "/api/persons/**")).hasAnyRole("USER", "ADMIN")
                        //.requestMatchers(antMatcher(HttpMethod.DELETE, "/api/persons/**")).hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                            //antMatcher(HttpMethod.GET, "/error"),
                            //antMatcher(HttpMethod.GET, "/login"),
                            antMatcher("/h2-console/**"),
                            //antMatcher("/index.html"),
                            //antMatcher(HttpMethod.GET, "/favicon.ico"),
                            // regexMatcher(".*\\?x=y")).hasRole("SPECIAL"),
                            antMatcher(HttpMethod.GET, "/v3/**"),
                            antMatcher(HttpMethod.GET, "/swagger-ui.html"),
                            antMatcher(HttpMethod.GET, "/swagger-ui/**")).permitAll()
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

        http.headers(headers -> headers.frameOptions().sameOrigin());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    /**
     * webSecurityCustomizer does not use the filter chain. Spring Boot produces a warning: You are asking Spring Security to ignore Ant [pattern='/js/**', GET]. T his is not recommended -- please use
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
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());
        manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());
        manager.createUser(User.withUsername("admin@example.com").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());
        manager.createUser(User.withUsername("admin@admin.ch").password(passwordEncoder().encode("admin")).roles("ADMIN", "USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
