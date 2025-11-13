package com.tpi.gestion.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "PARAMETROS")
public class Parametros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARAMETRO_ID")
    private long parametroId;

    @Column(name = "VALOR_LITRO_COMBUSTIBLE")
    private long valorLitroCombustible;
    
    @Column(name = "CARGOS_FIJOS")
    private long cargosFijos;
}
