package com.tpi.logistica.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RutaTentativaRequestDTO {

    private String origenDireccion;
    private Double origenLatitud;
    private Double origenLongitud;

    private String destinoDireccion;
    private Double destinoLatitud;
    private Double destinoLongitud;

    // Opcional: depósitos intermedios, etc.
    private List<String> depositosIntermedios;
}
