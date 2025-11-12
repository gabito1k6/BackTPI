package com.tpi.logistica.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "CONTENEDOR")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENEDOR_ID")
    private Long contenedorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_ACTUAL")
    private EstadoContenedor estadoActual;

    @Column(name = "PESO")
    private Double peso;

    @Column(name = "VOLUMEN")
    private Double volumen;
}

