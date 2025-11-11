package com.tpi.logistica.repo;

import com.tpi.logistica.models.Tramo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TramoRepository extends JpaRepository<Tramo, Long> {

    List<Tramo> findByRuta_RutaIdOrderByOrdenAsc(Long rutaId);

    Page<Tramo> findByEstadoTramo_Nombre(String nombre, Pageable pageable);

    Page<Tramo> findByRuta_RutaId(Long rutaId, Pageable pageable);

    Page<Tramo> findByCamionId(Long camionId, Pageable pageable);

    Page<Tramo> findByCamionIdAndEstadoTramo_Nombre(Long camionId, String nombre, Pageable pageable);
}



