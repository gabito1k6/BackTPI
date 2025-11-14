package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.CamionEstadoDTO;
import com.tpi.logistica.models.Contenedor;
import com.tpi.logistica.models.EstadoContenedor;
import com.tpi.logistica.models.Solicitud;
import com.tpi.logistica.repo.ContenedorRepository;
import com.tpi.logistica.service.CamionEstadoProxyService;
import com.tpi.logistica.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistica/reportes")
@RequiredArgsConstructor
public class ReportesOperativosController {

    private final CamionEstadoProxyService camionEstadoProxyService;
    private final ContenedorRepository contenedorRepository;
    private final SolicitudService solicitudService;

    /**
     * GET /logistica/reportes/contenedores/pendientes?depositoId=&page=&size=
     * Muestra los contenedores aún no entregados.
     * (depositoId se mantiene por enunciado, aunque hoy no exista el campo).
     */
    @GetMapping("/contenedores/pendientes")
    public Page<Contenedor> getContenedoresPendientes(
            @RequestParam(required = false) Long depositoId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return contenedorRepository.findByEstadoActualNot(EstadoContenedor.ENTREGADO, pageable);
    }

    /**
     * GET /logistica/reportes/camiones/estado?disponible=true&page=&size=
     * Reporte de camiones por estado (disponible / no disponible).
     */
    @GetMapping("/camiones/estado")
    public Page<CamionEstadoDTO> getCamionesPorEstado(
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return camionEstadoProxyService.obtenerCamionesPorEstado(disponible, page, size);
    }

    /**
     * GET /logistica/reportes/solicitudes?estado=&desde=&hasta=&clienteId=&page=&size=
     *
     * Permite obtener informes de solicitudes:
     *  - por estado,
     *  - por rango de fechas (desde/hasta, formateadas como yyyy-MM-dd),
     *  - por cliente,
     *  - o combinaciones de lo anterior.
     */
    @GetMapping("/solicitudes")
    public Page<Solicitud> getReporteSolicitudes(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return solicitudService.buscarConFiltros(estado, desde, hasta, clienteId, pageable);
    }
}
