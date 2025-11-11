package com.tpi.logistica.repo;

import com.tpi.logistica.models.Ruta;
import com.tpi.logistica.models.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    // Para validar que una solicitud no tenga ruta asignada
    Optional<Ruta> findBySolicitud(Solicitud solicitud);

    // Buscar una única ruta por solicitud (caso uso asignación)
    Optional<Ruta> findBySolicitud_SolicitudId(Long solicitudId);

    // GET /logistica/rutas?solicitudId=...
    Page<Ruta> findBySolicitud_SolicitudId(Long solicitudId, Pageable pageable);
}
