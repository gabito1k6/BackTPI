package com.tpi.logistica.service;

import com.tpi.logistica.DTO.OsrmResponseDTO;
import com.tpi.logistica.DTO.OsrmRouteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeolocalizacionService {

    private final RestTemplate restTemplate;
    
    // URL interna de Docker: 'osrm' es el nombre del servicio en docker-compose
    private final String OSRM_API_URL = "http://osrm:5000/route/v1/driving/";

    public GeolocalizacionService() {
        this.restTemplate = new RestTemplate();
    }

    public OsrmRouteDTO calcularRuta(Double lat1, Double lon1, Double lat2, Double lon2) {
        // OSRM usa el formato: longitud,latitud (al revés de Google)
        String coordenadas = String.format("%f,%f;%f,%f", lon1, lat1, lon2, lat2);
        
        // overview=false hace la respuesta más liviana (no devuelve la geometría del mapa)
        String url = OSRM_API_URL + coordenadas + "?overview=false";

        try {
            OsrmResponseDTO response = restTemplate.getForObject(url, OsrmResponseDTO.class);
            
            if (response != null && "Ok".equals(response.getCode()) && !response.getRoutes().isEmpty()) {
                return response.getRoutes().get(0); // Devolvemos la mejor ruta encontrada
            }
        } catch (Exception e) {
            System.err.println("Error conectando con OSRM: " + e.getMessage());
        }
        return null;
    }
}