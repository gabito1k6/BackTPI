package com.tpi.logistica.repo;

import com.tpi.logistica.models.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // ⬇⬇⬇ NECESARIO PARA RutaTramoService (seguimiento por contenedor)
    Optional<Solicitud> findTopByContenedor_ContenedorIdOrderBySolicitudIdDesc(
            Long contenedorId
    );

    // ---------- FILTROS PARA EL ENDPOINT /logistica/solicitudes ----------

    // Filtro solo por estado
    Page<Solicitud> findByEstadoSolicitud_Nombre(
            String estado,
            Pageable pageable
    );

    // Filtro solo por fechas
    Page<Solicitud> findByFechaCreacionBetween(
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    );

    // Filtro por estado + fechas
    Page<Solicitud> findByEstadoSolicitud_NombreAndFechaCreacionBetween(
            String estado,
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    );

    // Filtro por cliente
    Page<Solicitud> findByCliente_ClienteId(
            Long clienteId,
            Pageable pageable
    );

    // Estado + Cliente
    Page<Solicitud> findByEstadoSolicitud_NombreAndCliente_ClienteId(
            String estado,
            Long clienteId,
            Pageable pageable
    );

    // Fechas + Cliente
    Page<Solicitud> findByFechaCreacionBetweenAndCliente_ClienteId(
            LocalDateTime desde,
            LocalDateTime hasta,
            Long clienteId,
            Pageable pageable
    );

    // Estado + Fechas + Cliente
    Page<Solicitud> findByEstadoSolicitud_NombreAndFechaCreacionBetweenAndCliente_ClienteId(
            String estado,
            LocalDateTime desde,
            LocalDateTime hasta,
            Long clienteId,
            Pageable pageable
    );
}



