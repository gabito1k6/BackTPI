package com.tpi.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                
                // 1. Ruta para servicio-gestion
                .route("servicio-gestion-route", r -> r.path("/gestion/**") // Si la URL empieza con /gestion/
                        .filters(f -> f.stripPrefix(1)) // Le quita el /gestion
                        .uri("http://servicio-gestion:8081")) // Lo manda a este contenedor
                        //.uri("http://localhost:8081"))//Para pruebas con eco

                // 2. Ruta para servicio-logistica
                .route("servicio-logistica-route", r -> r.path("/logistica/**") // Si empieza con /logistica/
                        .filters(f -> f.stripPrefix(1)) // Le quita el /logistica
                        .uri("http://servicio-logistica:8082")) // Lo manda a este contenedor
                        //.uri("http://postman-echo.com"))//Para pruebas con eco
                
                // 3. Ruta para las integraciones (manejada por logística)
                .route("servicio-integraciones-route", r -> r.path("/integraciones/**")
                        //.filters(f -> f.stripPrefix(1))
                        .uri("http://servicio-logistica:8082"))
                        //.uri("http://postman-echo.com"))//Para pruebas con eco

                .build();
    }
}