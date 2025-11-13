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

import com.tpi.gestion.DTO.TarifaCreationDTO;
import com.tpi.gestion.DTO.TarifaResponseDTO;
import com.tpi.gestion.service.TarifaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tarifa")
@RequiredArgsConstructor
public class TarifaController {
    
    private final TarifaService tarifaService;

    @PostMapping
    public ResponseEntity<TarifaResponseDTO> crearTarifa(
        @Valid @RequestBody TarifaCreationDTO tarifaDTO 
    ){
        TarifaResponseDTO tarifaCreada = tarifaService.crearTarifa(tarifaDTO);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(tarifaCreada);
    }



    @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminarTarifa(@PathVariable("id") Long id){
    tarifaService.eliminarTarifa(id);

    return ResponseEntity.noContent().build();
   }

    @PutMapping("/{id}")
   public ResponseEntity<TarifaResponseDTO> actualizarTarifa(
    @PathVariable("id") Long id,
    @Valid @RequestBody TarifaCreationDTO dto) {
        TarifaResponseDTO tarifaAct = tarifaService.actualizarTarifa(id, dto);

        return ResponseEntity.ok(tarifaAct);
   }


   @GetMapping("/{id}")
   public ResponseEntity<TarifaResponseDTO> buscarTarifaPorId(
    @PathVariable("id") long id){
        TarifaResponseDTO tarifaDb = tarifaService.buscarTarifaPorId(id);
        return ResponseEntity.ok(tarifaDb);
    }

    @GetMapping
    public ResponseEntity<Page<TarifaResponseDTO>> listarTodos(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
    ){
        Page<TarifaResponseDTO> response = tarifaService.listarTarifas(page, size);

        return ResponseEntity.ok(response);
    }
 








}
