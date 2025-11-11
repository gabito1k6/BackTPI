package com.tpi.logistica.DTO;

import lombok.Data;

@Data
public class CambiarEstadoSolicitudDTO {
    private Long solicitudId;
    private Long estadoSolicitudId;
}

