package com.tpi.logistica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                
                // 1. Rutas públicas (Swagger)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                
                // 2. Rutas de CLIENTE 
                .requestMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/solicitudes").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/solicitudes/{id}/estado").hasRole("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/seguimiento/**").hasRole("CLIENTE")

                // 3. Rutas de TRANSPORTISTA 
                .requestMatchers(HttpMethod.GET, "/tramos/asignados").hasRole("TRANSPORTISTA")
                .requestMatchers(HttpMethod.PUT, "/tramos/{id}/iniciar").hasRole("TRANSPORTISTA")
                .requestMatchers(HttpMethod.PUT, "/tramos/{id}/finalizar").hasRole("TRANSPORTISTA")

                // 4. Rutas de ADMIN (prácticamente todo lo demás) 
                // (hasRole("ADMIN") también permite acceder a las rutas de Cliente y Transportista si el traductor funciona bien)
                .requestMatchers("/solicitudes/**", "/rutas/**", "/tramos/**", "/clientes/**", "/contenedores/**").hasRole("ADMIN")
                
                // 5. Ruta de Integraciones (Google Maps) - Asumimos que solo el ADMIN la usa para calcular rutas
                .requestMatchers("/integraciones/**").hasRole("ADMIN")

                // 6. Cualquier otra petición debe estar autenticada
                .anyRequest().authenticated()
            )
            // 7. Le decimos a Spring que use nuestro traductor custom
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // --- EL TRADUCTOR DE ROLES (Idéntico al de servicio-gestion) ---
    @Bean
    @SuppressWarnings("unchecked")
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> {
            
            final Map<String, Object> realmAccess;
            if (jwt.getClaims().containsKey("realm_access") && 
                jwt.getClaims().get("realm_access") instanceof Map) {
                realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            } else {
                return java.util.Collections.emptySet(); 
            }

            final Collection<String> roles;
            if (realmAccess.containsKey("roles") && 
                realmAccess.get("roles") instanceof Collection) {
                roles = ((Collection<?>) realmAccess.get("roles")).stream()
                        .filter(role -> role instanceof String)
                        .map(role -> (String) role)
                        .collect(Collectors.toSet());
            } else {
                return java.util.Collections.emptySet();
            }

            return roles.stream()
                    .map(roleName -> "ROLE_" + roleName) // Agrega el prefijo ROLE_
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        };

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
