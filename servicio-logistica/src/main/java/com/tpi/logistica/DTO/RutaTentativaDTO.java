package com.tpi.logistica.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RutaTentativaDTO {

    private String descripcion;
    private Double costoEstimado;
    private Long tiempoEstimadoMinutos;
}
