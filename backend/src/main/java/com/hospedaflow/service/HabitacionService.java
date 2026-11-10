package com.hospedaflow.service;

import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.model.enums.EstadoHabitacion;
import com.hospedaflow.Repositorio.HabitacionJsonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitacionService {

    private final HabitacionJsonRepository habitacionRepository;

    public HabitacionService(HabitacionJsonRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public Habitacion crearHabitacion(Habitacion habitacion) {
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación no puede ser nula");
        }
        if (habitacion.getNumero() == null || habitacion.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de habitación es obligatorio");
        }
        if (habitacion.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio de la habitación no puede ser negativo");
        }
        if (habitacion.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }

        // Validación de duplicados por número
        boolean existeNumero = habitacionRepository.obtenerTodas().stream()
                .anyMatch(h -> h.getNumero().equalsIgnoreCase(habitacion.getNumero().trim())
                        && (habitacion.getId() == null || !h.getId().equals(habitacion.getId())));
        if (existeNumero) {
            throw new IllegalArgumentException("Ya existe una habitación con el número: " + habitacion.getNumero());
        }

        // Asignación de ID si es nuevo
        if (habitacion.getId() == null) {
            long maxId = habitacionRepository.obtenerTodas().stream()
                    .mapToLong(h -> h.getId() != null ? h.getId() : 0L)
                    .max()
                    .orElse(0L);
            habitacion.setId(maxId + 1);
        }

        if (habitacion.getEstado() == null) {
            habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
        }

        habitacionRepository.guardar(habitacion);
        return habitacion;
    }

    public void eliminarHabitacion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        obtenerPorId(id); // Valida que exista
        habitacionRepository.eliminar(id);
    }

    public List<Habitacion> consultarDisponibilidad() {
        return habitacionRepository.obtenerTodas().stream()
                .filter(Habitacion::estaDisponible)
                .collect(Collectors.toList());
    }

    public List<Habitacion> obtenerTodas() {
        return habitacionRepository.obtenerTodas();
    }

    public Habitacion obtenerPorId(Long id) {
        return habitacionRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + id));
    }

    public Habitacion cambiarEstado(Long id, EstadoHabitacion nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        Habitacion habitacion = obtenerPorId(id);
        habitacion.cambiarEstado(nuevoEstado);
        habitacionRepository.guardar(habitacion);
        return habitacion;
    }
}

