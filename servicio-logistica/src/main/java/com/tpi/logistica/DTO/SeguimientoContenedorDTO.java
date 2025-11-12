package com.tpi.logistica.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoContenedorDTO {

    private Long contenedorId;
    private String estadoContenedor;

    private Long solicitudId;
    private String estadoSolicitud;

    // Historial de tramos de la ruta asociada
    private List<SeguimientoTramoDTO> tramos;
}
