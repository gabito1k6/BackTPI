package com.tpi.gestion.DTO;

import com.tpi.gestion.enums.EstadoContenedor;

public record ContenedorPendienteDTO(
        Long contenedorId,
        Double peso,
        Double volumen,
        EstadoContenedor estadoActual,
        Long depositoId   
) { }
