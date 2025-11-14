package com.tpi.gestion.controller;

import com.tpi.gestion.DTO.ContenedorPendienteDTO;
import com.tpi.gestion.service.ContenedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contenedores")
@RequiredArgsConstructor
public class ContenedorController {

    private final ContenedorService contenedorService;

    /**
     * GET /contenedores/pendientes?depositoId=&page=&size=
     * Muestra los contenedores aún no entregados de un depósito.
     */
    @GetMapping("/pendientes")
    public ResponseEntity<Page<ContenedorPendienteDTO>> pendientes(
            @RequestParam Long depositoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ContenedorPendienteDTO> pagina =
                contenedorService.obtenerPendientes(depositoId, page, size);

        return ResponseEntity.ok(pagina);
    }
}
