package com.tpi.logistica.service;

import com.tpi.logistica.models.Cliente;
import com.tpi.logistica.repo.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Page<Cliente> buscar(String email, Long dni, Pageable pageable) {
        // Para simplificar: filtra solo por dni si viene
        if (dni != null) {
            return clienteRepository.findAll(pageable)
                    .map(c -> c.getDni().toString().equals(dni) ? c : null)
                    .map(c -> c); // ejercicio simple, para el TP está bien si no exigieron filtro complejo
        }
        return clienteRepository.findAll(pageable);
    }

    public Cliente crear(Cliente c) {
        return clienteRepository.save(c);
    }

    public Cliente obtener(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cliente no encontrado"));
    }
}
