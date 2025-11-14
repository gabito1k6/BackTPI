package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.CambiarEstadoSolicitudDTO;
import com.tpi.logistica.DTO.CrearSolicitudDTO;
import com.tpi.logistica.DTO.EstimacionSolicitudDTO;
import com.tpi.logistica.models.Solicitud;
import com.tpi.logistica.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    /**
     * GET /logistica/solicitudes?page=&size=
     *
     * Listado simple de solicitudes (sin filtros de reporte).
     * Los filtros por estado / fecha / cliente se manejan en
     * ReportesOperativosController bajo /logistica/reportes/solicitudes.
     */
    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public Page<Solicitud> listar(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return solicitudService.listar(pageable);
    }

    /**
     * GET /logistica/solicitudes/{id}
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public Solicitud obtener(@PathVariable Long id) {
        return solicitudService.obtenerPorId(id);
    }

    /**
     * POST /logistica/solicitudes
     */
    @PostMapping
    // @PreAuthorize("hasRole('CLIENTE')")
    public Solicitud crear(@RequestBody CrearSolicitudDTO dto) {
        return solicitudService.crear(dto);
    }

    /**
     * GET /logistica/solicitudes/{id}/estimacion
     * Devuelve costoEstimado y tiempoEstimado (en segundos).
     */
    @GetMapping("/{id}/estimacion")
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public EstimacionSolicitudDTO obtenerEstimacion(@PathVariable Long id) {
        return solicitudService.obtenerEstimacion(id);
    }

    /**
     * PUT /logistica/solicitudes/{id}/estado
     */
    @PutMapping("/{id}/estado")
    // @PreAuthorize("hasRole('ADMIN',"CLIENTE")")
    public Solicitud cambiarEstado(@PathVariable Long id,
                                   @RequestBody CambiarEstadoSolicitudDTO dto) {
        return solicitudService.cambiarEstado(id, dto);
    }
}


