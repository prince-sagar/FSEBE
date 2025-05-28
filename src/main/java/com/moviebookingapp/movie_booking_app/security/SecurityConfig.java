package com.moviebookingapp.movie_booking_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1.0/moviebooking/login",
                                "/api/v1.0/moviebooking/register",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api/v1.0/moviebooking/all"
                        ).permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1.0/moviebooking/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1.0/moviebooking/**").authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}