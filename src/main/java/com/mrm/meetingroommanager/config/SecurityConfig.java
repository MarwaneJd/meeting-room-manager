package com.mrm.meetingroommanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // correct way to disable CSRF in Spring 6.1+
                .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // allow all requests without auth
                )
                .httpBasic(Customizer.withDefaults()); // optional: enable basic auth
        return http.build();
    }
}
