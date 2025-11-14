package com.tpi.logistica.repo;

import com.tpi.logistica.models.Contenedor;
import com.tpi.logistica.models.EstadoContenedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    Page<Contenedor> findByEstadoActual(EstadoContenedor estado, Pageable pageable);
    Page<Contenedor> findByEstadoActualNot(EstadoContenedor estado, Pageable pageable);

}

