package com.tpi.logistica.service;

import com.tpi.logistica.DTO.CamionEstadoDTO;
import com.tpi.logistica.DTO.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CamionEstadoProxyService {

    private final WebClient gestionWebClient;

    public Page<CamionEstadoDTO> obtenerCamionesPorEstado(Boolean disponible,
                                                          Integer page,
                                                          Integer size) {

        final int pageSafe = (page == null || page < 0) ? 0 : page;
        final int sizeSafe = (size == null || size <= 0) ? 10 : size;

        ParameterizedTypeReference<RestPage<CamionEstadoDTO>> typeRef =
                new ParameterizedTypeReference<>() {};

        // Armamos la request base
        RestPage<CamionEstadoDTO> restPage = gestionWebClient
                .get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/camiones")
                            .queryParam("page", pageSafe)
                            .queryParam("size", sizeSafe);

                    // disponible = true -> solo DISPONIBLE (se filtra en gestión)
                    if (Boolean.TRUE.equals(disponible)) {
                        builder.queryParam("disponibilidad", "DISPONIBLE");
                    }
                    // disponible = false o null -> NO mandamos disponibilidad
                    // para traer todos y filtrar acá si hace falta.
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(typeRef)
                .block();

        // Si no hay filtro de disponibilidad, devolvemos lo que vino
        if (disponible == null) {
            return restPage;
        }

        // disponible = true -> ya está filtrado en gestión
        if (Boolean.TRUE.equals(disponible)) {
            return restPage;
        }

        // disponible = false -> queremos EN_VIAJE y EN_MANTENIMIENTO
        List<CamionEstadoDTO> filtrados = restPage.getContent().stream()
                .filter(c -> !"DISPONIBLE".equalsIgnoreCase(c.getDisponibilidad()))
                .collect(Collectors.toList());

        return new PageImpl<>(
                filtrados,
                PageRequest.of(pageSafe, sizeSafe),
                filtrados.size()
        );
    }
}


