package com.tpi.logistica.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "RUTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUTA_ID")
    private Long rutaId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOLICITUD_ID", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Solicitud solicitud;

    @OneToMany(mappedBy = "ruta", fetch = FetchType.LAZY)
    @JsonIgnore // para no entrar en bucle Ruta -> Tramos -> Ruta
    private List<Tramo> tramos;
}
