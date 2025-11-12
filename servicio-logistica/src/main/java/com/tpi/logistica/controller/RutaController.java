package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.CrearTramoDTO;
import com.tpi.logistica.DTO.RutaTentativaDTO;
import com.tpi.logistica.DTO.RutaTentativaRequestDTO;
import com.tpi.logistica.models.Ruta;
import com.tpi.logistica.models.Tramo;
import com.tpi.logistica.repo.RutaRepository;
import com.tpi.logistica.service.RutaTramoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaRepository rutaRepository;
    private final RutaTramoService rutaTramoService;

    /**
     * GET /logistica/rutas (ADMIN)
     * Lista rutas con filtro opcional por solicitudId.
     */
    @GetMapping("/rutas")
    // @PreAuthorize("hasRole('ADMIN')")
    public Page<Ruta> listarRutas(
            @RequestParam(required = false) Long solicitudId,
            @PageableDefault(size = 20) Pageable pageable) {

        // Si viene solicitudId, buscamos sólo la ruta de esa solicitud
        if (solicitudId != null) {
        return rutaRepository.findBySolicitud_SolicitudId(solicitudId)
            .<Page<Ruta>>map(ruta -> new PageImpl<>(
                    Collections.singletonList(ruta),
                    pageable,
                    1
            ))
            .orElse(Page.empty(pageable).map(r -> (Ruta) r));
}



        // Si no hay filtro, todas las rutas paginadas
        return rutaRepository.findAll(pageable);
    }

    /**
     * POST /logistica/rutas/tentativas (ADMIN)
     * Devuelve rutas tentativas con costo/tiempo estimado.
     * (usa la lógica que tengas en RutaTramoService.calcularRutasTentativas)
     */
    @PostMapping("/tentativas")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<RutaTentativaDTO> obtenerRutasTentativas(
            @RequestBody RutaTentativaRequestDTO request) {
        return rutaTramoService.calcularRutasTentativas(request);
    }

    /**
     * POST /logistica/solicitudes/{solicitudId}/ruta (ADMIN)
     * Asigna una ruta definitiva a una solicitud creando los tramos.
     */
    @PostMapping("/solicitudes/{solicitudId}/ruta")
    // @PreAuthorize("hasRole('ADMIN')")
    public Ruta asignarRutaDefinitiva(
            @PathVariable Long solicitudId,
            @RequestBody List<CrearTramoDTO> tramosDto) {

        return rutaTramoService.crearRutaConTramos(solicitudId, tramosDto);
    }

    /**
     * GET /logistica/rutas/{rutaId}/tramos (ADMIN)
     * Devuelve los tramos ordenados de una ruta.
     */
    @GetMapping("/{rutaId}/tramos")
    // @PreAuthorize("hasRole('ADMIN')")
    public List<Tramo> obtenerTramosPorRuta(@PathVariable Long rutaId) {
        return rutaTramoService.obtenerTramosPorRuta(rutaId);
    }
}
