// package com.tpi.logistica.controller;

// import com.tpi.logistica.DTO.ContenedorPendienteDTO;
// import com.tpi.logistica.service.ContenedorPendienteProxyService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/logistica/contenedores")
// @RequiredArgsConstructor
// public class LogisticaContenedorController {

//     private final ContenedorPendienteProxyService contenedorPendienteProxyService;

//     /**
//      * GET /logistica/contenedores/pendientes?depositoId=&page=&size=
//      * Muestra los contenedores aún no entregados.
//      */
//     @GetMapping("/pendientes")
//     public ResponseEntity<Page<ContenedorPendienteDTO>> pendientes(
//             @RequestParam Long depositoId,
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size
//     ) {
//         Page<ContenedorPendienteDTO> pagina =
//                 contenedorPendienteProxyService.obtenerContenedoresPendientes(depositoId, page, size);

//         return ResponseEntity.ok(pagina);
//     }
// }
