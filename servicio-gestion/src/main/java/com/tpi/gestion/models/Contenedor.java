package com.tpi.gestion.models;

import com.tpi.gestion.enums.EstadoContenedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "CONTENEDOR")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENEDOR_ID")
    private Long contenedorId;

    @Column(name = "PESO")
    private Double peso;

    @Column(name = "VOLUMEN")
    private Double volumen;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_ACTUAL")
    private EstadoContenedor estadoActual;  
}

