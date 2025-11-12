package com.tpi.logistica.DTO;

import lombok.Data;

@Data
public class CrearSolicitudDTO {

    private Long clienteId;
    private Long contenedorId;

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

}
