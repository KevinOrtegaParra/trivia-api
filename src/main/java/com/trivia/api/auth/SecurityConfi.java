package com.trivia.api.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.trivia.api.auth.filter.JwtAuthenticationFilter;
import com.trivia.api.auth.filter.JwtAuthorizationFilter;
import com.trivia.api.auth.jwt.JwtUtils;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfi {

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Autowired
        JwtAuthorizationFilter jwtAuthorizationFilter;

        @Value("${fron.url}")
        private String frontend;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
                        throws Exception {

                JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
                jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

                return http
                                .csrf(csrf -> csrf
                                                .disable())
                                .cors(cors -> {
                                })
                                .authorizeHttpRequests(authRequest -> authRequest
                                                .requestMatchers("/auth/**", "/i18n/**", "/content/**",
                                                                "/users/register", "/h2-console/**", "/swagger-ui/**",
                                                                "/swagger-ui/index.html", "/swagger-ui.html",
                                                                "/v3/api-docs",
                                                                "/v3/api-docs/**", "/test/**", "/stripe/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(sessionManager -> sessionManager
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilter(jwtAuthenticationFilter)
                                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();

        }

        @Bean
        public CorsFilter corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.setAllowedOrigins(List.of(frontend));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));
                source.registerCorsConfiguration("/**", config);
                return new CorsFilter(source);
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
                        throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder);

                return authenticationManagerBuilder.build();

        }
}