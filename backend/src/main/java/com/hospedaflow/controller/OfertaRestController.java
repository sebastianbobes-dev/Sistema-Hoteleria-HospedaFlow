package com.hospedaflow.controller;

import com.hospedaflow.dto.OfertaRequestDTO;
import com.hospedaflow.model.entity.Oferta;
import com.hospedaflow.service.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = "*")
public class OfertaRestController {

    @Autowired
    private OfertaService ofertaService;

    @GetMapping
    public ResponseEntity<List<Oferta>> listarTodas() {
        return ResponseEntity.ok(ofertaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<?> crearOferta(@RequestBody OfertaRequestDTO dto) {
        try {
            LocalDate inicio = LocalDate.parse(dto.getFechaInicio());
            LocalDate fin = LocalDate.parse(dto.getFechaFin());
            Oferta oferta = ofertaService.crearOferta(
                dto.getTitulo(), dto.getDescripcion(), dto.getPorcentajeDescuento(), inicio, fin, dto.getHabitacionId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(oferta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOferta(@PathVariable Long id) {
        try {
            ofertaService.eliminarOferta(id);
            return ResponseEntity.ok("Oferta eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
