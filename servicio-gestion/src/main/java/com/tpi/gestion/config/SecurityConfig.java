// package com.tpi.gestion.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.jwt.Jwt;
// import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
// import org.springframework.security.web.SecurityFilterChain;

// import java.util.Collection;
// import java.util.Map;
// import java.util.stream.Collectors;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(authorize -> authorize
                
//                 // 1. Rutas públicas (Swagger)
//                 .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                
//                 // 2. Rutas de Gestión (SOLO ADMIN)
//                 .requestMatchers("/**").hasRole("ADMIN") 
                
//                 .anyRequest().authenticated()
//             )
//             // 3. Le decimos a Spring que use nuestro traductor custom
//             .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            
//             .csrf(csrf -> csrf.disable());

//         return http.build();
//     }

//     // --- EL TRADUCTOR DE ROLES (Versión sin warnings) ---
//     @Bean
//     @SuppressWarnings("unchecked") // Suprimimos la advertencia solo donde es inevitable
//     public JwtAuthenticationConverter jwtAuthenticationConverter() {
//         JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
//         // Creamos un converter custom que sabe leer de "realm_access"
//         Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> {
            
//             // 1. Ir a buscar el claim "realm_access"
//             final Map<String, Object> realmAccess;
//             if (jwt.getClaims().containsKey("realm_access") && 
//                 jwt.getClaims().get("realm_access") instanceof Map) {
                
//                 realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
//             } else {
//                 return java.util.Collections.emptySet(); // No hay 'realm_access', no hay roles
//             }

//             // 2. De ahí, sacar la lista "roles"
//             final Collection<String> roles;
//             if (realmAccess.containsKey("roles") && 
//                 realmAccess.get("roles") instanceof Collection) {
                
//                 roles = ((Collection<?>) realmAccess.get("roles")).stream()
//                         .filter(role -> role instanceof String)
//                         .map(role -> (String) role)
//                         .collect(Collectors.toSet());
//             } else {
//                 return java.util.Collections.emptySet(); // 'realm_access' no tiene 'roles'
//             }

//             // 3. Mapear cada rol a "ROLE_ROL"
//             return roles.stream()
//                     .map(roleName -> "ROLE_" + roleName) // Agrega el prefijo ROLE_
//                     .map(SimpleGrantedAuthority::new)
//                     .collect(Collectors.toSet());
//         };

//         converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
//         return converter;
//     }
// }

package com.tpi.gestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 😴 Para pruebas: sin CSRF
            .csrf(AbstractHttpConfigurer::disable)

            // 🔓 TODO permitido
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/h2-console/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().permitAll()
            )

            // Necesario para ver H2
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )

            // Sin login básico ni form
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

