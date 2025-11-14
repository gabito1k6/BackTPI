package com.tpi.gestion.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tpi.gestion.DTO.CamionCreationDTO;
import com.tpi.gestion.DTO.CamionResponseDTO;
import com.tpi.gestion.service.CamionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/camiones")
@RequiredArgsConstructor
public class CamionController {

    private final CamionService camionService;

    // GET /camiones?disponibilidad=&pesoMin=&volumenMin=&page=&size=
    @GetMapping
    public ResponseEntity<Page<CamionResponseDTO>> listar(
            @RequestParam(required = false) String disponibilidad,
            @RequestParam(required = false) Double pesoMin,
            @RequestParam(required = false) Double volumenMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CamionResponseDTO> paginas =
                camionService.filtrarCamiones(disponibilidad, pesoMin, volumenMin, page, size);

        return ResponseEntity.ok(paginas);
    }

    // 👉 NUEVO ENDPOINT: /camiones/estado?disponible=true&page=0&size=10
    @GetMapping("/estado")
    public ResponseEntity<Page<CamionResponseDTO>> listarPorEstado(
            @RequestParam Boolean disponible,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Si disponible = true, filtramos por DISPONIBLE; si es false, dejamos sin filtro
        String disponibilidad = (disponible != null && disponible) ? "DISPONIBLE" : null;

        Page<CamionResponseDTO> paginas =
                camionService.filtrarCamiones(disponibilidad, null, null, page, size);

        return ResponseEntity.ok(paginas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamion(@PathVariable("id") Long id) {
        camionService.eliminarCamion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamionResponseDTO> actualizarCamion(
            @PathVariable("id") Long id,
            @Valid @RequestBody CamionCreationDTO dto) {
        CamionResponseDTO camionAct = camionService.actualizarCamion(id, dto);
        return ResponseEntity.ok(camionAct);
    }

    @PostMapping
    public ResponseEntity<CamionResponseDTO> crearCamion(
            @Valid @RequestBody CamionCreationDTO camionDto
    ) {
        CamionResponseDTO camionCreado = camionService.crearNuevoCamion(camionDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(camionCreado);
    }
}

