package main.java.com.tpi.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                
                // 1. Rutas públicas (Swagger)
                // Ojo: los paths son completos (incluyendo el prefijo)
                .pathMatchers("/gestion/swagger-ui/**", "/gestion/v3/api-docs/**").permitAll()
                .pathMatchers("/logistica/swagger-ui/**", "/logistica/v3/api-docs/**").permitAll()
                
                // 2. TODAS las demás rutas exigen autenticación
                .anyExchange().authenticated()
            )
            // 3. Habilitar validación JWT simple (sin traductor de roles)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}