// src/main/java/com/tpi/logistica/DTO/EstimacionSolicitudDTO.java
package com.tpi.logistica.DTO;

public record EstimacionSolicitudDTO(
        Long solicitudId,
        Double costoEstimado,
        Long tiempoEstimadoSegundos
) {
}
