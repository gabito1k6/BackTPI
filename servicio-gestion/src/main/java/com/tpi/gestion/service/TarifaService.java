package com.tpi.gestion.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tpi.gestion.DTO.TarifaCreationDTO;
import com.tpi.gestion.DTO.TarifaResponseDTO;
import com.tpi.gestion.models.Tarifa;
import com.tpi.gestion.repo.TarifaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;


    private TarifaResponseDTO mapToResponseDTO(Tarifa t){
        return new TarifaResponseDTO(
            t.getTarifaId(),
            t.getDescripcion(),
            t.getValor(),
            t.getRangoMin(),
            t.getRangoMax()
        );
    }

    @Transactional
    public TarifaResponseDTO crearTarifa(TarifaCreationDTO dto){
        Tarifa tarifa = Tarifa.builder()
                            .descripcion(dto.descripcion())
                            .valor(dto.valor())
                            .rangoMin(dto.rangMin())
                            .rangoMax(dto.rangoMax())
                            .build();
        Tarifa tarifaDb = tarifaRepository.save(tarifa);
        return this.mapToResponseDTO(tarifaDb);
    }


    @Transactional
    public void eliminarTarifa(long id){
        var tarifa = tarifaRepository.findById(id);
        if(tarifa.isPresent()){
            tarifaRepository.deleteById(id);
            return;
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND);
    }
    

    @Transactional
    public TarifaResponseDTO actualizarTarifa(Long id, TarifaCreationDTO t){
        Tarifa tarifa_db = tarifaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND
        ));
        tarifa_db.setDescripcion(t.descripcion());
        tarifa_db.setRangoMax(t.rangoMax());
        tarifa_db.setRangoMin(t.rangMin());
        tarifa_db.setValor(t.valor());

        tarifaRepository.save(tarifa_db);

        return this.mapToResponseDTO(tarifa_db);
    }


    public TarifaResponseDTO buscarTarifaPorId(Long id){
        Optional<Tarifa> tarifaDb = tarifaRepository.findById(id);
        if (tarifaDb.isPresent()){
            return this.mapToResponseDTO(tarifaDb.get());
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND);
    }


    public Page<TarifaResponseDTO> listarTarifas(int size, int page){
            Pageable pageable = PageRequest.of(page, size);
            return tarifaRepository
                    .findAll(pageable)
                    .map(this::mapToResponseDTO);
    }

    
}
