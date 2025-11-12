package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.SeguimientoContenedorDTO;
import com.tpi.logistica.DTO.SeguimientoTramoDTO;
import com.tpi.logistica.service.RutaTramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguimiento")
@RequiredArgsConstructor
public class SeguimientoController {

    private final RutaTramoService rutaTramoService;

    /**
     * GET /logistica/seguimiento/solicitud/{id}
     * Devuelve el detalle de tramos de la solicitud.
     */
    @GetMapping("/solicitud/{id}")
    public List<SeguimientoTramoDTO> seguimientoPorSolicitud(@PathVariable Long id) {
        return rutaTramoService.obtenerSeguimientoPorSolicitud(id);
    }

    /**
     * GET /logistica/seguimiento/{contenedorId}
     * Devuelve estado actual + historial de tramos.
     */
    @GetMapping("/{contenedorId}")
    public SeguimientoContenedorDTO seguimientoPorContenedor(@PathVariable Long contenedorId) {
        return rutaTramoService.obtenerSeguimientoPorContenedor(contenedorId);
    }
}
