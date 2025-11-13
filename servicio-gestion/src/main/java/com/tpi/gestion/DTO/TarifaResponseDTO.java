package com.tpi.gestion.DTO;

public record TarifaResponseDTO(
    long tarifaId,

    String descripcion,

    Double valor,

    Double rangoMin,

    Double rangoMax
) {
}
