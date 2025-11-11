package com.tpi.logistica.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoTramoDTO {

    private Long tramoId;
    private Integer orden;

    private String origenDescripcion;
    private String destinoDescripcion;

    private String estado;    // nombre del EstadoTramo
    private Long camionId;

    private Date fechaInicioReal;
    private Date fechaFinReal;
}
