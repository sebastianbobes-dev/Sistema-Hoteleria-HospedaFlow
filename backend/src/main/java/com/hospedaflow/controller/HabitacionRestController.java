package com.hospedaflow.controller;

import com.hospedaflow.dto.HabitacionRequestDTO;
import com.hospedaflow.model.entity.Habitacion;
import com.hospedaflow.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
public class HabitacionRestController {

    @Autowired
    private HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<Habitacion>> listarTodas() {
        return ResponseEntity.ok(habitacionService.consultarDisponibilidad());
    }

    @PostMapping
    public ResponseEntity<?> crearHabitacion(@RequestBody HabitacionRequestDTO dto) {
        try {
            Habitacion creada = habitacionService.crearHabitacion(
                dto.getNumero(), dto.getTipo(), dto.getPrecioBase(), dto.getCapacidad()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHabitacion(@PathVariable Long id) {
        try {
            habitacionService.eliminarHabitacion(id);
            return ResponseEntity.ok("Habitación eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
