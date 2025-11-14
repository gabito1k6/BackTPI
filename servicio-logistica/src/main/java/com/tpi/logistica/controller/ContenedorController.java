package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.ContenedorPendienteDTO;
import com.tpi.logistica.models.Contenedor;
import com.tpi.logistica.models.EstadoContenedor;
import com.tpi.logistica.repo.ContenedorRepository;
import com.tpi.logistica.service.ContenedorPendienteProxyService;
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
    private final ContenedorPendienteProxyService contenedorPendienteProxyService;

    /**
     * GET /logistica/contenedores?estado=&page=&size=
     * Lista contenedores propios de LOGÍSTICA, con filtro opcional por estado.
     */
    @GetMapping
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
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> obtener(@PathVariable Long id) {
        return contenedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * POST /logistica/contenedores
     * (solo para pruebas locales, si querés crear contenedores en LOGÍSTICA)
     */
    @PostMapping
    public ResponseEntity<Contenedor> crear(@RequestBody Contenedor contenedor) {
        Contenedor creado = contenedorRepository.save(contenedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /**
     * GET /logistica/contenedores/pendientes?depositoId=&page=&size=
     * Usa el servicio de GESTIÓN vía WebClient (ContenedorPendienteProxyService)
     * y devuelve los ContenedorPendienteDTO.
     */
   
}

