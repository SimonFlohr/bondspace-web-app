package com.bondspace.config;

import com.bondspace.filter.SessionValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class SecurityConfig {

    @Bean
    public SessionValidationFilter sessionValidationFilter() {
        return new SessionValidationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection if not needed (adjust for production as required)
                .csrf(csrf -> csrf.disable())

                // Configure URL access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Public endpoints
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated() // Secure other endpoints
                )

                // Basic authentication
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
