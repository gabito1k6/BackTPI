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

import com.tpi.gestion.DTO.ParametroDTO;
import com.tpi.gestion.service.ParametrosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parametros")
@RequiredArgsConstructor
public class ParametrosController {
    
 private final ParametrosService parametrosService;

    @PostMapping
    public ResponseEntity<ParametroDTO> crearParametro(
        @Valid @RequestBody ParametroDTO parametroDTO 
    ){
        ParametroDTO parametroCreado = parametrosService.crearParametro(parametroDTO);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(parametroCreado);
    }



    @DeleteMapping("/{id}")
   public ResponseEntity<Void> eliminarParametro(@PathVariable("id") Long id){
    parametrosService.eliminarParametro(id);

    return ResponseEntity.noContent().build();
   }

    @PutMapping("/{id}")
   public ResponseEntity<ParametroDTO> actualizarParametro(
    @PathVariable("id") Long id,
    @Valid @RequestBody ParametroDTO dto) {
        ParametroDTO parametroAct = parametrosService.actualizarParametro(id, dto);

        return ResponseEntity.ok(parametroAct);
   }


   @GetMapping("/{id}")
   public ResponseEntity<ParametroDTO> buscarParametroPorId(
    @PathVariable("id") long id){
        ParametroDTO parametroDb = parametrosService.buscarParametroPorId(id);
        return ResponseEntity.ok(parametroDb);
    }

    @GetMapping
    public ResponseEntity<Page<ParametroDTO>> listarTodos(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
    ){
        Page<ParametroDTO> response = parametrosService.listarTodos(page, size);

        return ResponseEntity.ok(response);
                    
    }
 



}
