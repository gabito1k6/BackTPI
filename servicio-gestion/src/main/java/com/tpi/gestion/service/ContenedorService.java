package com.tpi.gestion.service;

import com.tpi.gestion.DTO.ContenedorPendienteDTO;
import com.tpi.gestion.enums.EstadoContenedor;
import com.tpi.gestion.models.Contenedor;
import com.tpi.gestion.repo.ContenedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    public Page<ContenedorPendienteDTO> obtenerPendientes(Long depositoId,
                                                          int page,
                                                          int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Contenedor> contenedores = contenedorRepository
                // Ajustá el estado según lo que signifique "aún no entregado" en tu enum
                .findByEstadoActual(EstadoContenedor.EN_TRANSITO, pageable);

        List<ContenedorPendienteDTO> contenido = contenedores
                .map(c -> new ContenedorPendienteDTO(
                        c.getContenedorId(),
                        c.getPeso(),
                        c.getVolumen(),
                        c.getEstadoActual(),
                        depositoId              // por ahora viene del parámetro
                ))
                .getContent();

        return new PageImpl<>(contenido, pageable, contenedores.getTotalElements());
    }
}
