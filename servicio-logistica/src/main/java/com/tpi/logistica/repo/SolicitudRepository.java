package com.tpi.logistica.repo;

import com.tpi.logistica.models.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Para seguimiento por contenedor (última solicitud de ese contenedor)
    Optional<Solicitud> findTopByContenedor_ContenedorIdOrderBySolicitudIdDesc(Long contenedorId);

    // Filtros para reportes/búsquedas
    Page<Solicitud> findByEstadoSolicitud_Nombre(String estado, Pageable pageable);

    Page<Solicitud> findByCliente_ClienteId(Long clienteId, Pageable pageable);

    Page<Solicitud> findByEstadoSolicitud_NombreAndCliente_ClienteId(
            String estado,
            Long clienteId,
            Pageable pageable
    );
}



