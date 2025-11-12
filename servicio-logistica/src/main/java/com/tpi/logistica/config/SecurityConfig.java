package com.tpi.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// NO usamos @EnableMethodSecurity mientras estamos probando sin auth
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Swagger / OpenAPI
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                // Consola H2
                .requestMatchers("/h2-console/**").permitAll()
                // TODO el API de logística abierto para pruebas
                .requestMatchers("/logistica/**", "/seguimiento/**").permitAll()
                // Cualquier otra cosa también la dejamos pasar por ahora
                .anyRequest().permitAll()
            )
            // Necesario para ver H2 en browser
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            // Sin login / basic / form
            .httpBasic(httpBasic -> {})
            .formLogin(form -> form.disable());

        return http.build();
    }
}
