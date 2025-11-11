package com.tpi.logistica.service;

import com.tpi.logistica.DTO.*;
import com.tpi.logistica.models.*;
import com.tpi.logistica.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaTramoService {

    private final SolicitudRepository solicitudRepository;
    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final EstadoTramoRepository estadoTramoRepository;

    /**
     * POST /logistica/solicitudes/{solicitudId}/ruta
     * Asigna una ruta definitiva a una solicitud creando sus tramos.
     */
    @Transactional
    public Ruta crearRutaConTramos(Long solicitudId, List<CrearTramoDTO> tramosDto) {

        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        // Verificar que la solicitud no tenga ya ruta definida
        rutaRepository.findBySolicitud(solicitud).ifPresent(r -> {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La solicitud ya tiene ruta definida"
            );
        });

        Ruta ruta = Ruta.builder()
                .solicitud(solicitud)
                .build();

        ruta = rutaRepository.save(ruta);

        int ordenAuto = 1;

        for (CrearTramoDTO dto : tramosDto) {

            EstadoTramo estado = null;
            if (dto.getEstadoTramoId() != null) {
                estado = estadoTramoRepository.findById(dto.getEstadoTramoId())
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, "EstadoTramo no encontrado"));
            }

            Tramo tramo = Tramo.builder()
                    .ruta(ruta)
                    .orden(dto.getOrden() != null ? dto.getOrden() : ordenAuto++)
                    .origenDescripcion(dto.getOrigenDescripcion())
                    .origenLatitud(dto.getOrigenLatitud())
                    .origenLongitud(dto.getOrigenLongitud())
                    .destinoDescripcion(dto.getDestinoDescripcion())
                    .destinoLatitud(dto.getDestinoLatitud())
                    .destinoLongitud(dto.getDestinoLongitud())
                    .tipoTramo(dto.getTipoTramo())
                    .camionId(dto.getCamionId())
                    .estadoTramo(estado)
                    .build();

            tramoRepository.save(tramo);
        }

        return ruta;
    }

    /**
     * GET /logistica/rutas/{rutaId}/tramos
     */
    public List<Tramo> obtenerTramosPorRuta(Long rutaId) {
        return tramoRepository.findByRuta_RutaIdOrderByOrdenAsc(rutaId);
    }

    /**
     * PUT /logistica/tramos/{id}/estado
     */
    @Transactional
    public Tramo actualizarEstadoTramo(Long tramoId, ActualizarEstadoTramoDTO dto) {
        Tramo tramo = tramoRepository.findById(tramoId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Tramo no encontrado"));

        EstadoTramo estado = estadoTramoRepository.findById(dto.getEstadoTramoId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "EstadoTramo no encontrado"));

        tramo.setEstadoTramo(estado);
        return tramoRepository.save(tramo);
    }

    /**
     * Lógica para GET /logistica/tramos
     * (ADMIN) - filtros opcionales: estado, camionId, rutaId
     */
    public Page<Tramo> buscarTramos(String estado,
                                    Long camionId,
                                    Long rutaId,
                                    Pageable pageable) {

        if (rutaId != null) {
            return tramoRepository.findByRuta_RutaId(rutaId, pageable);
        }

        if (camionId != null && estado != null && !estado.isBlank()) {
            return tramoRepository.findByCamionIdAndEstadoTramo_Nombre(camionId, estado, pageable);
        }

        if (camionId != null) {
            return tramoRepository.findByCamionId(camionId, pageable);
        }

        if (estado != null && !estado.isBlank()) {
            return tramoRepository.findByEstadoTramo_Nombre(estado, pageable);
        }

        return tramoRepository.findAll(pageable);
    }

    /**
     * Lógica para GET /logistica/mis-tramos
     * (TRANSPORTISTA) - usa camionId (por ahora pasado por parámetro)
     */
    public Page<Tramo> buscarMisTramos(Long camionId,
                                       String estado,
                                       Pageable pageable) {

        if (camionId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo determinar el camión del transportista autenticado"
            );
        }

        if (estado != null && !estado.isBlank()) {
            return tramoRepository.findByCamionIdAndEstadoTramo_Nombre(camionId, estado, pageable);
        }

        return tramoRepository.findByCamionId(camionId, pageable);
    }

    /**
     * Lógica para POST /logistica/rutas/tentativas
     * Implementación simplificada de rutas tentativas con costo/tiempo estimado.
     */
    public List<RutaTentativaDTO> calcularRutasTentativas(RutaTentativaRequestDTO request) {

        List<RutaTentativaDTO> result = new ArrayList<>();

        result.add(
                RutaTentativaDTO.builder()
                        .descripcion("Ruta directa: " +
                                request.getOrigenDireccion() + " -> " +
                                request.getDestinoDireccion())
                        .costoEstimado(1000.0)
                        .tiempoEstimadoMinutos(300L)
                        .build()
        );

        return result;
    }

    // ------------------------------------------------------------
    // SEGUIMIENTO
    // ------------------------------------------------------------

    public List<SeguimientoTramoDTO> obtenerSeguimientoPorSolicitud(Long solicitudId) {

        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        Ruta ruta = rutaRepository.findBySolicitud(solicitud)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La solicitud no tiene ruta asociada"));

        List<Tramo> tramos = tramoRepository.findByRuta_RutaIdOrderByOrdenAsc(ruta.getRutaId());

        return tramos.stream()
                .map(this::toSeguimientoTramoDTO)
                .collect(Collectors.toList());
    }

    public SeguimientoContenedorDTO obtenerSeguimientoPorContenedor(Long contenedorId) {

        Solicitud solicitud = solicitudRepository
                .findTopByContenedor_ContenedorIdOrderBySolicitudIdDesc(contenedorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El contenedor no tiene solicitudes asociadas"));

        Ruta ruta = rutaRepository.findBySolicitud(solicitud)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La solicitud no tiene ruta asociada"));

        List<Tramo> tramos = tramoRepository.findByRuta_RutaIdOrderByOrdenAsc(ruta.getRutaId());

        String estadoContenedor = solicitud.getContenedor() != null
                && solicitud.getContenedor().getEstadoActual() != null
                ? solicitud.getContenedor().getEstadoActual().name()
                : "DESCONOCIDO";

        String estadoSolicitud = solicitud.getEstadoSolicitud() != null
                ? solicitud.getEstadoSolicitud().getNombre()
                : "DESCONOCIDO";

        return SeguimientoContenedorDTO.builder()
                .contenedorId(contenedorId)
                .solicitudId(solicitud.getSolicitudId())
                .estadoContenedor(estadoContenedor)
                .estadoSolicitud(estadoSolicitud)
                .tramos(tramos.stream()
                        .map(this::toSeguimientoTramoDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Helper privado
    private SeguimientoTramoDTO toSeguimientoTramoDTO(Tramo t) {
        return SeguimientoTramoDTO.builder()
                .tramoId(t.getTramoId())
                .orden(t.getOrden())
                .origenDescripcion(t.getOrigenDescripcion())
                .destinoDescripcion(t.getDestinoDescripcion())
                .estado(t.getEstadoTramo() != null ? t.getEstadoTramo().getNombre() : null)
                .camionId(t.getCamionId())
                .fechaInicioReal(t.getFechaInicioReal())
                .fechaFinReal(t.getFechaFinReal())
                .build();
    }
}

