package com.tpi.logistica.service;

import com.tpi.logistica.DTO.ContenedorPendienteDTO;
import com.tpi.logistica.DTO.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class ContenedorPendienteProxyService {

    private final WebClient gestionWebClient;

    public Page<ContenedorPendienteDTO> obtenerContenedoresPendientes(
            Long depositoId,
            int page,
            int size
    ) {
        int pageSafe = (page < 0) ? 0 : page;
        int sizeSafe = (size <= 0) ? 10 : size;

        ParameterizedTypeReference<RestPage<ContenedorPendienteDTO>> typeRef =
                new ParameterizedTypeReference<>() {};

        RestPage<ContenedorPendienteDTO> restPage = gestionWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/contenedores/pendientes")   // endpoint en GESTIÓN
                        .queryParam("depositoId", depositoId)
                        .queryParam("page", pageSafe)
                        .queryParam("size", sizeSafe)
                        .build())
                .retrieve()
                .bodyToMono(typeRef)
                .block();

        return restPage;
    }
}

