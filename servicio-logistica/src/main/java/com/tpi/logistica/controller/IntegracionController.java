package com.tpi.logistica.controller;

import com.tpi.logistica.DTO.OsrmRouteDTO;
import com.tpi.logistica.service.GeolocalizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integraciones")
@RequiredArgsConstructor
public class IntegracionController {

    private final GeolocalizacionService geoService;

    // Mantenemos la ruta que pide el enunciado, aunque por detrás usemos OSRM
    @GetMapping("/google/directions") 
    public ResponseEntity<OsrmRouteDTO> obtenerDistancia(
            @RequestParam Double origenLat,
            @RequestParam Double origenLon,
            @RequestParam Double destinoLat,
            @RequestParam Double destinoLon) {
        
        OsrmRouteDTO ruta = geoService.calcularRuta(origenLat, origenLon, destinoLat, destinoLon);
        
        if (ruta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ruta);
    }
}