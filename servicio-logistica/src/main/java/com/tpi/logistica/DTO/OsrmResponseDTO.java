package com.tpi.logistica.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmResponseDTO {
    private String code; // Debería ser "Ok"
    private List<OsrmRouteDTO> routes; // OSRM puede devolver varias rutas, usaremos la primera
}