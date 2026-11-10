package com.hospedaflow.service;

import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.model.entidades.Reserva;
import com.hospedaflow.model.enums.EstadoHabitacion;
import com.hospedaflow.model.enums.EstadoReserva;
import com.hospedaflow.Repositorio.HabitacionJsonRepository;
import com.hospedaflow.Repositorio.ReservaJsonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ReservaService {

    private final ReservaJsonRepository reservaRepository;
    private final HabitacionJsonRepository habitacionRepository;

    public ReservaService(ReservaJsonRepository reservaRepository, HabitacionJsonRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public Reserva registrarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        validarRangoFechas(reserva.getFechaEntrada(), reserva.getFechaSalida());

        if (reserva.getHuesped() == null) {
            throw new IllegalArgumentException("El huésped es obligatorio para registrar la reserva");
        }

        // Si se asigna habitación al registrar, validar disponibilidad real
        if (reserva.getHabitacionAsignada() != null && reserva.getHabitacionAsignada().getId() != null) {
            Long habId = reserva.getHabitacionAsignada().getId();
            Habitacion hab = habitacionRepository.buscarPorId(habId)
                    .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + habId));
            reserva.setHabitacionAsignada(hab);

            if (!verificarDisponibilidad(habId, reserva.getFechaEntrada(), reserva.getFechaSalida(), null)) {
                throw new IllegalStateException("La habitación no se encuentra disponible para el rango de fechas indicado");
            }
        }

        // Generar código de reserva si no tiene
        if (reserva.getCodigoReserva() == null || reserva.getCodigoReserva().trim().isEmpty()) {
            reserva.setCodigoReserva("RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Asignar ID autoincremental si no tiene
        if (reserva.getId() == null) {
            long maxId = reservaRepository.obtenerTodas().stream()
                    .mapToLong(r -> r.getId() != null ? r.getId() : 0L)
                    .max()
                    .orElse(0L);
            reserva.setId(maxId + 1);
        }

        if (reserva.getEstado() == null) {
            reserva.setEstado(EstadoReserva.PENDIENTE);
        }

        // Cálculo de tarifa
        reserva.setPrecioTotal(calcularTarifa(reserva));

        reservaRepository.guardar(reserva);
        return reserva;
    }

    public Reserva modificarFechas(Long reservaId, LocalDate nuevaEntrada, LocalDate nuevaSalida) {
        if (reservaId == null) {
            throw new IllegalArgumentException("El ID de reserva es obligatorio");
        }
        validarRangoFechas(nuevaEntrada, nuevaSalida);

        Reserva reserva = obtenerPorId(reservaId);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No se puede modificar una reserva que se encuentra cancelada");
        }

        // Si tiene habitación asignada, verificar disponibilidad para las nuevas fechas
        if (reserva.getHabitacionAsignada() != null) {
            boolean disponible = verificarDisponibilidad(
                    reserva.getHabitacionAsignada().getId(),
                    nuevaEntrada,
                    nuevaSalida,
                    reserva.getId()
            );
            if (!disponible) {
                throw new IllegalStateException("La habitación asignada no está disponible en las nuevas fechas seleccionadas");
            }
        }

        reserva.modificarReserva(nuevaEntrada, nuevaSalida);
        reserva.setPrecioTotal(calcularTarifa(reserva));

        reservaRepository.actualizar(reserva);
        return reserva;
    }

    public Reserva confirmarReserva(Long reservaId) {
        if (reservaId == null) {
            throw new IllegalArgumentException("El ID de reserva es obligatorio");
        }
        Reserva reserva = obtenerPorId(reservaId);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No se puede confirmar una reserva que ha sido cancelada");
        }

        reserva.confirmarReserva();
        reservaRepository.actualizar(reserva);
        return reserva;
    }

    public Reserva cancelarReserva(Long reservaId) {
        if (reservaId == null) {
            throw new IllegalArgumentException("El ID de reserva es obligatorio");
        }
        Reserva reserva = obtenerPorId(reservaId);
        reserva.cancelarReserva();
        reservaRepository.actualizar(reserva);
        return reserva;
    }

    public Reserva asignarHabitacion(Long reservaId, Long habitacionId) {
        if (reservaId == null || habitacionId == null) {
            throw new IllegalArgumentException("Los identificadores de reserva y habitación son obligatorios");
        }
        Reserva reserva = obtenerPorId(reservaId);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No se puede asignar habitación a una reserva cancelada");
        }

        Habitacion habitacion = habitacionRepository.buscarPorId(habitacionId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + habitacionId));

        if (!verificarDisponibilidad(habitacionId, reserva.getFechaEntrada(), reserva.getFechaSalida(), reserva.getId())) {
            throw new IllegalStateException("La habitación no está disponible para las fechas de la reserva");
        }

        reserva.asignarHabitacion(habitacion);
        reserva.setPrecioTotal(calcularTarifa(reserva));

        reservaRepository.actualizar(reserva);
        return reserva;
    }

    public double calcularTarifa(Reserva reserva) {
        if (reserva == null || reserva.getHabitacionAsignada() == null) {
            return 0.0;
        }

        LocalDate entrada = reserva.getFechaEntrada();
        LocalDate salida = reserva.getFechaSalida();
        if (entrada == null || salida == null) {
            return 0.0;
        }

        long dias = ChronoUnit.DAYS.between(entrada, salida);
        if (dias <= 0) {
            dias = 1; // Mínimo 1 noche
        }

        double precioBasePorNoche = reserva.getHabitacionAsignada().getPrecio();
        double subtotal = dias * precioBasePorNoche;

        // Descuento si aplica oferta vigente
        Oferta oferta = reserva.getOfertaAplicada();
        if (oferta != null && oferta.esVigente()) {
            double desc = oferta.getDescuento();
            double factorDescuento = desc > 1.0 ? (desc / 100.0) : desc;
            double montoDescuento = subtotal * factorDescuento;
            subtotal = Math.max(0.0, subtotal - montoDescuento);
        }

        return subtotal;
    }

    public boolean verificarDisponibilidad(Long habitacionId, LocalDate entrada, LocalDate salida, Long reservaExcluidaId) {
        if (habitacionId == null || entrada == null || salida == null) {
            return false;
        }

        Habitacion habitacion = habitacionRepository.buscarPorId(habitacionId).orElse(null);
        if (habitacion == null || habitacion.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
            return false;
        }

        List<Reserva> reservasExistentes = reservaRepository.obtenerTodas();
        for (Reserva r : reservasExistentes) {
            // Excluir la reserva que se está modificando o reservas canceladas
            if (reservaExcluidaId != null && reservaExcluidaId.equals(r.getId())) {
                continue;
            }
            if (r.getEstado() == EstadoReserva.CANCELADA) {
                continue;
            }
            if (r.getHabitacionAsignada() != null && habitacionId.equals(r.getHabitacionAsignada().getId())) {
                // Solapamiento: entrada < r.salida && salida > r.entrada
                if (entrada.isBefore(r.getFechaSalida()) && salida.isAfter(r.getFechaEntrada())) {
                    return false; // Conflicto de fechas
                }
            }
        }

        return true;
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepository.obtenerTodas();
    }

    public Reserva obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));
    }

    private void validarRangoFechas(LocalDate entrada, LocalDate salida) {
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Las fechas de entrada y salida son obligatorias");
        }
        if (!entrada.isBefore(salida)) {
            throw new IllegalArgumentException("La fecha de entrada debe ser estrictamente anterior a la fecha de salida");
        }
    }
}
