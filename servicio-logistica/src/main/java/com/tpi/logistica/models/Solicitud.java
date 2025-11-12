package com.tpi.logistica.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "SOLICITUD")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOLICITUD_ID")
    private Long solicitudId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTENEDOR_ID")
    private Contenedor contenedor;

    @Column(name = "ORIGEN_DIRECCION")
    private String origenDireccion;

    @Column(name = "ORIGEN_LATITUD")
    private Double origenLatitud;

    @Column(name = "ORIGEN_LONGITUD")
    private Double origenLongitud;

    @Column(name = "DESTINO_DIRECCION")
    private String destinoDireccion;

    @Column(name = "DESTINO_LATITUD")
    private Double destinoLatitud;

    @Column(name = "DESTINO_LONGITUD")
    private Double destinoLongitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTADO_SOLICITUD_ID")
    private EstadoSolicitud estadoSolicitud;

    @Column(name = "COSTO_ESTIMADO")
    private Double costoEstimado;

    @Column(name = "TIEMPO_ESTIMADO")
    private Duration tiempoEstimado;

    @Column(name = "COSTO_FINAL")
    private Double costoFinal;

    @Column(name = "TIEMPO_REAL")
    private Duration tiempoReal;
}
