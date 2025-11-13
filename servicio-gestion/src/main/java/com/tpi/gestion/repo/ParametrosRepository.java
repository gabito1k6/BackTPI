package com.tpi.gestion.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tpi.gestion.models.Parametros;

public interface ParametrosRepository  extends JpaRepository<Parametros, Long>{
    
}
