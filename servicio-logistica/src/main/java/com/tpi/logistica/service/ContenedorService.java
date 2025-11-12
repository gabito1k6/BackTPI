package com.tpi.logistica.service;

import com.tpi.logistica.models.Contenedor;
import com.tpi.logistica.models.EstadoContenedor;
import com.tpi.logistica.repo.ContenedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    public Page<Contenedor> listarContenedores(String estado, Pageable pageable) {
        if (estado != null) {
            try {
                EstadoContenedor estadoEnum = EstadoContenedor.valueOf(estado.toUpperCase());
                return contenedorRepository.findByEstadoActual(estadoEnum, pageable);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido: " + estado);
            }
        }
        return contenedorRepository.findAll(pageable);
    }

    public Contenedor crearContenedor(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    public Contenedor actualizarEstado(Long id, String nuevoEstado) {
        Contenedor contenedor = contenedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenedor no encontrado"));
        try {
            EstadoContenedor estadoEnum = EstadoContenedor.valueOf(nuevoEstado.toUpperCase());
            contenedor.setEstadoActual(estadoEnum);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido: " + nuevoEstado);
        }
        return contenedorRepository.save(contenedor);
    }

    public void eliminarContenedor(Long id) {
        if (!contenedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenedor no encontrado");
        }
        contenedorRepository.deleteById(id);
    }
}

