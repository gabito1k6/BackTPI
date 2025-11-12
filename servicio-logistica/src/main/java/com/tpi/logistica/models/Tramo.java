package com.tpi.logistica.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TRAMO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Evita problemas con proxies LAZY al serializar
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAMO_ID")
    private Long tramoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RUTA_ID")
    // Cuando devolvemos un Tramo, de la Ruta sólo nos interesa info básica,
    // evitamos meter "tramos" y "solicitud" completos para no generar ciclos.
    @JsonIgnoreProperties({"tramos", "solicitud", "hibernateLazyInitializer", "handler"})
    private Ruta ruta;

    // Camión asignado (id del servicio-gestion)
    @Column(name = "CAMION_ID")
    private Long camionId;

    @Column(name = "ORDEN")
    private Integer orden;

    @Column(name = "ORIGEN_DESCRIPCION")
    private String origenDescripcion;

    @Column(name = "ORIGEN_LATITUD")
    private Double origenLatitud;

    @Column(name = "ORIGEN_LONGITUD")
    private Double origenLongitud;

    @Column(name = "DESTINO_DESCRIPCION")
    private String destinoDescripcion;

    @Column(name = "DESTINO_LATITUD")
    private Double destinoLatitud;

    @Column(name = "DESTINO_LONGITUD")
    private Double destinoLongitud;

    @Column(name = "TIPO_TRAMO")
    private String tipoTramo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTADO_TRAMO_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EstadoTramo estadoTramo;

    @Column(name = "COSTO_APROXIMADO")
    private Double costoAprox;

    @Column(name = "COSTO_REAL")
    private Double costoReal;

    @Column(name = "FECHA_INICIO_ESTIMADA")
    private Date fechaInicioEstimada;

    @Column(name = "FECHA_FIN_ESTIMADA")
    private Date fechaFinEstimada;

    @Column(name = "FECHA_INICIO_REAL")
    private Date fechaInicioReal;

    @Column(name = "FECHA_FIN_REAL")
    private Date fechaFinReal;
}
