package com.tpi.logistica.controller;

import com.tpi.logistica.models.Contenedor;
import com.tpi.logistica.models.EstadoContenedor;
import com.tpi.logistica.repo.ContenedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contenedores")
@RequiredArgsConstructor
public class ContenedorController {

    private final ContenedorRepository contenedorRepository;

    /**
     * GET /logistica/contenedores?estado=&page=&size=
     * Lista contenedores, con filtro opcional por estado.
     * (Enunciado menciona depositoId, pero tu entidad no lo tiene, así que no se filtra por eso.)
     */
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public Page<Contenedor> listar(
            @RequestParam(required = false) EstadoContenedor estado,
            @PageableDefault(size = 20) Pageable pageable) {

        if (estado != null) {
            return contenedorRepository.findByEstadoActual(estado, pageable);
        }
        return contenedorRepository.findAll(pageable);
    }

    /**
     * GET /logistica/contenedores/{id}
     * ADMIN o CLIENTE dueño (sin seguridad por ahora).
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<Contenedor> obtener(@PathVariable Long id) {
        return contenedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * (Opcional) POST /logistica/contenedores
     * Solo si querés crear contenedores desde este servicio.
     * Muy útil para pruebas locales.
     */
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Contenedor> crear(@RequestBody Contenedor contenedor) {
        Contenedor creado = contenedorRepository.save(contenedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
