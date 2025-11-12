package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.CambiarEstadoSolicitudDTO;
import com.tpi.logistica.DTO.CrearSolicitudDTO;
import com.tpi.logistica.models.Solicitud;
import com.tpi.logistica.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistica/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    // GET /logistica/solicitudes?estado=&clienteId=
    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public Page<Solicitud> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 20) Pageable pageable) {

        // Si no mandan filtros, devuelvo todo
        if ((estado == null || estado.isBlank()) && clienteId == null) {
            return solicitudService.listar(pageable);
        }

        // Si hay al menos un filtro, uso buscar(...)
        return solicitudService.buscar(estado, clienteId, pageable);
    }

    // GET /logistica/solicitudes/{id}
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public Solicitud obtener(@PathVariable Long id) {
        return solicitudService.obtenerPorId(id);
    }

    // POST /logistica/solicitudes
    // (por ahora sin seguridad para probar)
    @PostMapping
    // @PreAuthorize("hasRole('CLIENTE')")
    public Solicitud crear(@RequestBody CrearSolicitudDTO dto) {
        return solicitudService.crear(dto);
    }

    // PUT /logistica/solicitudes/{id}/estado
    @PutMapping("/{id}/estado")
    // @PreAuthorize("hasRole('ADMIN')")
    public Solicitud cambiarEstado(@PathVariable Long id,
                                   @RequestBody CambiarEstadoSolicitudDTO dto) {
        return solicitudService.cambiarEstado(id, dto);
    }
}

