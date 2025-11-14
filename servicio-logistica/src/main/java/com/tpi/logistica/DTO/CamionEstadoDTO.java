package com.tpi.logistica.DTO;

import lombok.Data;

/**
 * DTO que usamos en Logística para leer la lista de camiones
 * que viene desde servicio-gestion (/camiones).
 *
 * No hace falta que tenga TODOS los campos del JSON, solo los que te interesen.
 * Los demás campos serán ignorados por Jackson.
 */
@Data
public class CamionEstadoDTO {

    private Long camionId;
    private String patente;
    private Double capacidadPeso;
    private Double capacidadVolumen;

    // Lo traemos como String porque en gestión es un enum (DISPONIBLE / OCUPADO)
    private String disponibilidad;
}
