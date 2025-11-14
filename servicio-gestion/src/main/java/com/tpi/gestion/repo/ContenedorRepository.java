package com.tpi.gestion.repo;

import com.tpi.gestion.enums.EstadoContenedor;
import com.tpi.gestion.models.Contenedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {

    // Buscamos por estado solamente (el modelo no tiene depósito todavía)
    Page<Contenedor> findByEstadoActual(EstadoContenedor estadoActual, Pageable pageable);
}
