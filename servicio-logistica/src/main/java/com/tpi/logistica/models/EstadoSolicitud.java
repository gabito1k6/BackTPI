package com.tpi.logistica.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "ESTADO_SOLICITUD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EstadoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTADO_SOLICITUD_ID")
    private Long estadoSolicitudId;

    @Column(name = "NOMBRE", unique = true, nullable = false)
    private String nombre;
}
