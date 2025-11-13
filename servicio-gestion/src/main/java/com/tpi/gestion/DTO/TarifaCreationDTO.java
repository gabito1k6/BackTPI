package com.tpi.gestion.DTO;

import jakarta.validation.constraints.NotNull;

public record TarifaCreationDTO(
    String descripcion,

    @NotNull(message = "El valor es obligatorio")
    Double valor,

    @NotNull(message = "El rango minimo es obligatorio")
    Double rangMin,

    @NotNull(message = "El rango maximo es obligatorio")
    Double rangoMax
){
}
