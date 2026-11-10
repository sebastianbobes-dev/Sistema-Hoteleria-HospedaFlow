package com.hospedaflow.controller;

import com.hospedaflow.dto.ReservaRequestDTO;
import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.model.entidades.Huesped;
import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.model.entidades.Reserva;
import com.hospedaflow.model.entidades.Usuario;
import com.hospedaflow.service.HabitacionService;
import com.hospedaflow.service.OfertaService;
import com.hospedaflow.service.PerfilService;
import com.hospedaflow.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints de reservas. Las reglas (fechas, disponibilidad, tarifa) viven en ReservaService.
 *   GET /api/reservas                                  todas
 *   GET /api/reservas/{id}                             una reserva
 *   POST /api/reservas                                 registra (201)
 *   PUT /api/reservas/{id}/fechas                      modifica fechas (body: fechaEntrada, fechaSalida)
 *   PUT /api/reservas/{id}/confirmar                   confirma
 *   PUT /api/reservas/{id}/cancelar                    cancela
 *   PUT /api/reservas/{id}/habitacion/{habitacionId}   asigna una habitacion
 */
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaRestController {

    private final ReservaService reservaService;
    private final PerfilService perfilService;
    private final HabitacionService habitacionService;
    private final OfertaService ofertaService;

    @Autowired
    public ReservaRestController(ReservaService reservaService, PerfilService perfilService,
                                 HabitacionService habitacionService, OfertaService ofertaService) {
        this.reservaService = reservaService;
        this.perfilService = perfilService;
        this.habitacionService = habitacionService;
        this.ofertaService = ofertaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Reserva> registrar(@RequestBody ReservaRequestDTO dto) {
        Usuario usuario = perfilService.obtenerPerfil(dto.getHuespedId());
        if (!(usuario instanceof Huesped huesped)) {
            throw new IllegalArgumentException("El usuario indicado no es un huésped");
        }

        Reserva reserva = new Reserva(null, dto.getCodigoReserva(), dto.getFechaEntrada(),
                dto.getFechaSalida(), null, huesped);

        if (dto.getHabitacionId() != null) {
            Habitacion habitacion = habitacionService.obtenerPorId(dto.getHabitacionId());
            reserva.setHabitacionAsignada(habitacion);
        }
        if (dto.getOfertaId() != null) {
            Oferta oferta = ofertaService.obtenerPorId(dto.getOfertaId());
            reserva.setOfertaAplicada(oferta);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.registrarReserva(reserva));
    }

    @PutMapping("/{id}/fechas")
    public ResponseEntity<Reserva> modificarFechas(@PathVariable Long id, @RequestBody ReservaRequestDTO dto) {
        return ResponseEntity.ok(reservaService.modificarFechas(id, dto.getFechaEntrada(), dto.getFechaSalida()));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.confirmarReserva(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    @PutMapping("/{id}/habitacion/{habitacionId}")
    public ResponseEntity<Reserva> asignarHabitacion(@PathVariable Long id, @PathVariable Long habitacionId) {
        return ResponseEntity.ok(reservaService.asignarHabitacion(id, habitacionId));
    }
}
