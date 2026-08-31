package com.abhimanyu.jobportal.config;

import com.abhimanyu.jobportal.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/users"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/jobs",
                                "/jobs/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/jobs"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/jobs/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/jobs/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/applications"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/applications",
                                "/applications/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/applications/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/applications/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers("/users/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}