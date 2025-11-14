package com.tpi.logistica.service;

import com.tpi.logistica.DTO.CambiarEstadoSolicitudDTO;
import com.tpi.logistica.DTO.CrearSolicitudDTO;
import com.tpi.logistica.DTO.EstimacionSolicitudDTO;
import com.tpi.logistica.models.*;
import com.tpi.logistica.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final ClienteRepository clienteRepository;
    private final ContenedorRepository contenedorRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;

    // Listado simple (sin filtros)
    public Page<Solicitud> listar(Pageable pageable) {
        return solicitudRepository.findAll(pageable);
    }

    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
    }

    // Crear solicitud recibiendo IDs existentes
    public Solicitud crear(CrearSolicitudDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Contenedor contenedor = contenedorRepository.findById(dto.getContenedorId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenedor no encontrado"));

        // Estado inicial opcional (si existe ID=1 => PENDIENTE)
        EstadoSolicitud estadoInicial = estadoSolicitudRepository.findById(1L).orElse(null);

        Solicitud solicitud = Solicitud.builder()
                .cliente(cliente)
                .contenedor(contenedor)
                .origenDireccion(dto.getOrigenDireccion())
                .origenLatitud(dto.getOrigenLatitud())
                .origenLongitud(dto.getOrigenLongitud())
                .destinoDireccion(dto.getDestinoDireccion())
                .destinoLatitud(dto.getDestinoLatitud())
                .destinoLongitud(dto.getDestinoLongitud())
                .estadoSolicitud(estadoInicial)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return solicitudRepository.save(solicitud);
    }

    public Solicitud cambiarEstado(Long id, CambiarEstadoSolicitudDTO dto) {
        Solicitud solicitud = obtenerPorId(id);

        EstadoSolicitud nuevoEstado = estadoSolicitudRepository.findById(dto.getEstadoSolicitudId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado de solicitud no encontrado"));

        solicitud.setEstadoSolicitud(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }

    /**
     * Búsqueda con filtros opcionales:
     * - estado  (nombre del EstadoSolicitud, ej: "PENDIENTE")
     * - desde / hasta (formato ISO: yyyy-MM-dd)
     * - clienteId
     */
    public Page<Solicitud> buscarConFiltros(String estado,
                                            String desdeStr,
                                            String hastaStr,
                                            Long clienteId,
                                            Pageable pageable) {

        // Parseo de fechas si vienen ambas
        LocalDateTime desde = null;
        LocalDateTime hasta = null;

        if (desdeStr != null && !desdeStr.isBlank()
                && hastaStr != null && !hastaStr.isBlank()) {

            LocalDate d = LocalDate.parse(desdeStr); // ej: 2025-01-01
            LocalDate h = LocalDate.parse(hastaStr); // ej: 2025-12-31

            desde = d.atStartOfDay();          // 2025-01-01T00:00
            hasta = h.atTime(23, 59, 59);      // 2025-12-31T23:59:59
        }

        boolean tieneEstado = (estado != null && !estado.isBlank());
        boolean tieneFechas = (desde != null && hasta != null);
        boolean tieneCliente = (clienteId != null);

        // Sin ningún filtro → todo
        if (!tieneEstado && !tieneFechas && !tieneCliente) {
            return solicitudRepository.findAll(pageable);
        }

        // Estado + Fechas + Cliente
        if (tieneEstado && tieneFechas && tieneCliente) {
            return solicitudRepository
                    .findByEstadoSolicitud_NombreAndFechaCreacionBetweenAndCliente_ClienteId(
                            estado, desde, hasta, clienteId, pageable
                    );
        }

        // Estado + Fechas
        if (tieneEstado && tieneFechas) {
            return solicitudRepository
                    .findByEstadoSolicitud_NombreAndFechaCreacionBetween(
                            estado, desde, hasta, pageable
                    );
        }

        // Estado + Cliente
        if (tieneEstado && tieneCliente) {
            return solicitudRepository
                    .findByEstadoSolicitud_NombreAndCliente_ClienteId(
                            estado, clienteId, pageable
                    );
        }

        // Fechas + Cliente
        if (tieneFechas && tieneCliente) {
            return solicitudRepository
                    .findByFechaCreacionBetweenAndCliente_ClienteId(
                            desde, hasta, clienteId, pageable
                    );
        }

        // Solo estado
        if (tieneEstado) {
            return solicitudRepository
                    .findByEstadoSolicitud_Nombre(estado, pageable);
        }

        // Solo fechas
        if (tieneFechas) {
            return solicitudRepository
                    .findByFechaCreacionBetween(desde, hasta, pageable);
        }

        // Solo cliente
        return solicitudRepository
                .findByCliente_ClienteId(clienteId, pageable);
    }

    public EstimacionSolicitudDTO obtenerEstimacion(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));

        Long segundos = null;
        if (solicitud.getTiempoEstimado() != null) {
            segundos = solicitud.getTiempoEstimado().getSeconds();
        }

        return new EstimacionSolicitudDTO(
                solicitud.getSolicitudId(),
                solicitud.getCostoEstimado(),
                segundos
        );
    }
}

