package com.example.demoinitial.config;

import com.example.demoinitial.auth.StatelessAuthenticationFilter;
import com.example.demoinitial.auth.StatelessLoginFilter;
import com.example.demoinitial.auth.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {

    private final UserDetailsService userDetailsService;
    private final TokenAuthenticationService tokenAuthenticationService;

   @Autowired
   public WebSecurityConfig(@Qualifier("appUserDetailsServiceImpl") UserDetailsService userDetailsService,
                            @Value("${hellorest.auth.secret:secretkey}") String secretKey,
                            @Value("${hellorest.auth.headername:xauth}") TokenAuthenticationService.AuthHeaderName  authHeaderName) {

     //   super(true);
        this.userDetailsService = userDetailsService;
        tokenAuthenticationService = new TokenAuthenticationService(authHeaderName, secretKey, userDetailsService);
   }

    @Configuration
    @Order(1)
    public class JwtRestApiSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();

            //h2 database console
            http.headers().frameOptions().disable()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.exceptionHandling()
                    .and().anonymous()
                    .and().servletApi()
                    .and().headers().cacheControl();

            http.antMatcher("/api/**").authorizeRequests()
                    .antMatchers("/api/login", "/api/authenticate").permitAll()
                    .antMatchers("/api/persons/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/h2-console/**").permitAll();

            http.addFilterBefore(
                    new StatelessLoginFilter("/api/login", tokenAuthenticationService, userDetailsService, authenticationManager()),
                    UsernamePasswordAuthenticationFilter.class);

            http.addFilterBefore(
                    new StatelessAuthenticationFilter(tokenAuthenticationService),
                    UsernamePasswordAuthenticationFilter.class);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/h2-console/**")
                    .antMatchers("/swagger-ui.html")
                    .antMatchers("/swagger-ui/**")
                    .antMatchers("/v3/**");

        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
            //auth.inMemoryAuthentication()
            //        .passwordEncoder(passwordEncoder())
            //        .withUser("user").password(passwordEncoder().encode("user")).roles("USER");
        }

        @Override
        protected UserDetailsService userDetailsService() {
            return userDetailsService;
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public TokenAuthenticationService tokenAuthenticationService() {
            return tokenAuthenticationService;
        }

        @Bean
        public CorsFilter corsFilter() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            final CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(false);
            config.addAllowedOrigin("*");
            config.addAllowedHeader("Authorization");
            config.addAllowedHeader("X-AUTH-TOKEN");
            config.addAllowedHeader("Content-Type");
            config.addAllowedMethod("OPTIONS");
            config.addAllowedMethod("GET");
            config.addAllowedMethod("POST");
            config.addAllowedMethod("PUT");
            config.addAllowedMethod("DELETE");
            config.addAllowedMethod("PATCH");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }
    }

    @Configuration
    @Order(2)
    public class WebMvcSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .mvcMatchers("/", "/login").permitAll()
                    .mvcMatchers("/users/**").hasRole("USER")
                    .mvcMatchers("/stomp-broadcast/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/h2-console/**")
                    .antMatchers("/swagger-ui.html")
                    .antMatchers("/swagger-ui/**")
                    .antMatchers("/v3/**");
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
            //auth.inMemoryAuthentication()
            //        .passwordEncoder(passwordEncoder())
            //        .withUser("user").password(passwordEncoder().encode("user")).roles("USER");
        }

        @Override
        protected UserDetailsService userDetailsService() {
            return userDetailsService;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
