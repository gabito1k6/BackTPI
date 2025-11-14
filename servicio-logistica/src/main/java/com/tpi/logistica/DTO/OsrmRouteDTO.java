package com.tpi.logistica.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora todo lo que no nos interesa del JSON
public class OsrmRouteDTO {
    private Double distance; // Distancia en metros
    private Double duration; // Tiempo en segundos
}