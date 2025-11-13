package com.tpi.gestion.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tpi.gestion.DTO.ParametroDTO;
import com.tpi.gestion.DTO.TarifaResponseDTO;
import com.tpi.gestion.models.Parametros;
import com.tpi.gestion.models.Tarifa;
import com.tpi.gestion.repo.ParametrosRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParametrosService {
    private final ParametrosRepository parametrosRepository;



    private ParametroDTO mapToResponseDTO(Parametros p){
        return new ParametroDTO(
            p.getValorLitroCombustible(),
            p.getCargosFijos()
        );
    }

    @Transactional
    public ParametroDTO crearParametro(ParametroDTO dto){
        Parametros parametro = Parametros.builder()
                                .cargosFijos(dto.cargosFijos())
                                .valorLitroCombustible(dto.TarifaCombustible())
                                .build();
        Parametros parametroDb = parametrosRepository.save(parametro);
        return this.mapToResponseDTO(parametroDb);
    }


    @Transactional
    public void eliminarParametro(long id){
        var parametro = parametrosRepository.findById(id);
        if(parametro.isPresent()){
            parametrosRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND);
    }
    

    @Transactional
    public ParametroDTO actualizarParametro(Long id, ParametroDTO t){
        var parametro_db = parametrosRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND
        ));
        parametro_db.setCargosFijos(t.cargosFijos());
        parametro_db.setValorLitroCombustible(t.TarifaCombustible());
        parametrosRepository.save(parametro_db);

        return this.mapToResponseDTO(parametro_db);
    }



    public Page<ParametroDTO> listarTodos(
        int page, int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return parametrosRepository
        .findAll(pageable)
        .map(this::mapToResponseDTO);
    }



    public ParametroDTO buscarParametroPorId(Long id){
        Optional<Parametros> parametroDb = parametrosRepository.findById(id);
        if (parametroDb.isPresent()){
            return this.mapToResponseDTO(parametroDb.get());
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND);
    }
}


