package com.hospedaflow.controller;

import com.hospedaflow.dto.HabitacionRequestDTO;
import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.model.enums.EstadoHabitacion;
import com.hospedaflow.service.HabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints de habitaciones. No contiene logica de negocio: convierte el DTO a
 * entidad, delega en HabitacionService y devuelve JSON.
 *   GET    /api/habitaciones                 todas (o ?disponibles=true solo las disponibles)
 *   GET    /api/habitaciones/{id}            una habitacion
 *   POST   /api/habitaciones                 registra (201)
 *   PUT    /api/habitaciones/{id}/estado     cambia el estado (body: { "estado": "MANTENIMIENTO" })
 *   DELETE /api/habitaciones/{id}            elimina
 */
@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "*")
public class HabitacionRestController {

    private final HabitacionService habitacionService;

    @Autowired
    public HabitacionRestController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public ResponseEntity<List<Habitacion>> listar(@RequestParam(defaultValue = "false") boolean disponibles) {
        return ResponseEntity.ok(disponibles
                ? habitacionService.consultarDisponibilidad()
                : habitacionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Habitacion> crear(@RequestBody HabitacionRequestDTO dto) {
        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(dto.getNumero());
        habitacion.setTipo(dto.getTipo());
        habitacion.setPrecio(dto.getPrecioBase());
        habitacion.setCapacidad(dto.getCapacidad());
        habitacion.setEstado(convertirEstado(dto.getEstado()));
        return ResponseEntity.status(HttpStatus.CREATED).body(habitacionService.crearHabitacion(habitacion));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Habitacion> cambiarEstado(@PathVariable Long id, @RequestBody HabitacionRequestDTO dto) {
        EstadoHabitacion estado = convertirEstado(dto.getEstado());
        return ResponseEntity.ok(habitacionService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return ResponseEntity.ok(Map.of("mensaje", "Habitación eliminada correctamente"));
    }

    /** Texto -> enum. Vacio = null (el servicio asigna DISPONIBLE). Valor desconocido = 400. */
    private EstadoHabitacion convertirEstado(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return EstadoHabitacion.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de habitación inválido: " + texto
                    + " (use DISPONIBLE, OCUPADA o MANTENIMIENTO)");
        }
    }
}
