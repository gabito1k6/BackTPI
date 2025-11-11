package com.tpi.logistica.service;

import com.tpi.logistica.DTO.CambiarEstadoSolicitudDTO;
import com.tpi.logistica.DTO.CrearSolicitudDTO;
import com.tpi.logistica.models.*;
import com.tpi.logistica.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // Crear solicitud recibiendo IDs existentes (versión simple)
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

    // Búsqueda con filtros opcionales: estado / clienteId
    public Page<Solicitud> buscar(String estado,
                                  Long clienteId,
                                  Pageable pageable) {

        boolean hayEstado = estado != null && !estado.isBlank();
        boolean hayCliente = clienteId != null;

        if (hayEstado && hayCliente) {
            return solicitudRepository
                    .findByEstadoSolicitud_NombreAndCliente_ClienteId(estado, clienteId, pageable);
        }

        if (hayEstado) {
            return solicitudRepository.findByEstadoSolicitud_Nombre(estado, pageable);
        }

        if (hayCliente) {
            return solicitudRepository.findByCliente_ClienteId(clienteId, pageable);
        }

        // Sin filtros -> todo
        return solicitudRepository.findAll(pageable);
    }
}

