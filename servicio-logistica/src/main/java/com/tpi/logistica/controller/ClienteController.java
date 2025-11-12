package com.tpi.logistica.controller;

import com.tpi.logistica.models.Cliente;
import com.tpi.logistica.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * GET /logistica/clientes?email=&dni=&page=&size=
     * - ADMIN (para el enunciado; sin seguridad activada por ahora)
     * Filtra por email o dni si vienen; si no, devuelve todos.
     */
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public Page<Cliente> listar(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long dni,
            @PageableDefault(size = 20) Pageable pageable) {

        return clienteService.buscar(email, dni, pageable);
    }

    /**
     * POST /logistica/clientes
     * - ADMIN o implícito vía solicitud (en nuestro caso: endpoint directo)
     */
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteService.crear(cliente);
    }

    /**
     * GET /logistica/clientes/{id}
     * - ADMIN
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public Cliente obtener(@PathVariable Long id) {
        return clienteService.obtener(id);
    }
}
