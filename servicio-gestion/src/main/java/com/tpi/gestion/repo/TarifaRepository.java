package com.tpi.gestion.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tpi.gestion.models.Tarifa;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Page<Tarifa> findAll(Pageable pageable);

    
}
