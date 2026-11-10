package com.hospedaflow.controller;

import com.hospedaflow.dto.ReservaRequestDTO;
import com.hospedaflow.model.entity.Reserva;
import com.hospedaflow.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaRestController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<?> registrarReserva(@RequestBody ReservaRequestDTO dto) {
        try {
            LocalDate entrada = LocalDate.parse(dto.getFechaEntrada());
            LocalDate salida = LocalDate.parse(dto.getFechaSalida());
            Reserva reserva = reservaService.registrarReserva(
                dto.getCodigoReserva(), entrada, salida, dto.getHuespedId(), dto.getHabitacionId(), dto.getOfertaId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            reservaService.cancelarReserva(id);
            return ResponseEntity.ok("Reserva cancelada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarReserva(@PathVariable Long id) {
        try {
            reservaService.confirmarReserva(id);
            return ResponseEntity.ok("Reserva confirmada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
