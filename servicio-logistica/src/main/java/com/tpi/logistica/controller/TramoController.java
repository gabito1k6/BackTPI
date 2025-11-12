package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.ActualizarEstadoTramoDTO;
import com.tpi.logistica.models.Tramo;
import com.tpi.logistica.service.RutaTramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final RutaTramoService rutaTramoService;

    /**
     * PUT /logistica/tramos/{id}/estado
     * Actualizar estado de un tramo
     * (ADMIN, TRANSPORTISTA en versión con seguridad)
     */
    @PutMapping("/tramos/{id}/estado")
    // @PreAuthorize("hasAnyRole('ADMIN','TRANSPORTISTA')")
    public Tramo actualizarEstado(@PathVariable Long id,
                                  @RequestBody ActualizarEstadoTramoDTO dto) {
        return rutaTramoService.actualizarEstadoTramo(id, dto);
    }

    /**
     * GET /logistica/tramos
     * Listado de tramos con filtros opcionales.
     * Filtros:
     *  - rutaId
     *  - camionId
     *  - estado (nombre del estado de tramo)
     * (ADMIN)
     */
    @GetMapping("/tramos")
    // @PreAuthorize("hasRole('ADMIN')")
    public Page<Tramo> buscarTramos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long camionId,
            @RequestParam(required = false, name = "rutaId") Long rutaId,
            @PageableDefault(size = 20) Pageable pageable) {

        return rutaTramoService.buscarTramos(estado, camionId, rutaId, pageable);
    }

    /**
     * GET /logistica/mis-tramos?camionId=&estado=
     * Tramos asignados a un camión (transportista).
     * En esta versión simplificada, el camionId viene por query param.
     * (TRANSPORTISTA)
     */
    @GetMapping("/mis-tramos")
    // @PreAuthorize("hasAnyRole('ADMIN','TRANSPORTISTA')")
    public Page<Tramo> misTramos(
            @RequestParam Long camionId,
            @RequestParam(required = false) String estado,
            @PageableDefault(size = 20) Pageable pageable) {

        return rutaTramoService.buscarMisTramos(camionId, estado, pageable);
    }
}
