package com.tpi.logistica.DTO;

import com.tpi.logistica.enums.EstadoContenedorLogistica;

public record ContenedorPendienteDTO(
        Long contenedorId,
        Double peso,
        Double volumen,
        EstadoContenedorLogistica estadoActual,
        Long depositoId,
        String depositoNombre
) {}
