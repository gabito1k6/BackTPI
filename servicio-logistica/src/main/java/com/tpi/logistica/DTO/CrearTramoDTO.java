package com.tpi.logistica.DTO;

import lombok.Data;

@Data
public class CrearTramoDTO {
    private Long rutaId;
    private Integer orden;
    private String origenDescripcion;
    private Double origenLatitud;
    private Double origenLongitud;
    private String destinoDescripcion;
    private Double destinoLatitud;
    private Double destinoLongitud;
    private String tipoTramo;
    private Long camionId;
    private Long estadoTramoId;
}

