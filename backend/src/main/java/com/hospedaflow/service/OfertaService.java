package com.hospedaflow.service;

import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.Repositorio.OfertaJsonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfertaService {

    private final OfertaJsonRepository ofertaRepository;

    public OfertaService(OfertaJsonRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }

    public Oferta crearOferta(Oferta oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException("La oferta no puede ser nula");
        }
        validarOferta(oferta);

        if (oferta.getId() == null) {
            long maxId = ofertaRepository.obtenerTodas().stream()
                    .mapToLong(o -> o.getId() != null ? o.getId() : 0L)
                    .max()
                    .orElse(0L);
            oferta.setId(maxId + 1);
        }

        ofertaRepository.guardar(oferta);
        return oferta;
    }

    public Oferta modificarOferta(Oferta oferta) {
        if (oferta == null || oferta.getId() == null) {
            throw new IllegalArgumentException("La oferta y su ID son obligatorios para modificar");
        }
        obtenerPorId(oferta.getId()); // Valida que exista
        validarOferta(oferta);

        ofertaRepository.guardar(oferta);
        return oferta;
    }

    public void eliminarOferta(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la oferta no puede ser nulo");
        }
        obtenerPorId(id); // Valida que exista
        ofertaRepository.eliminar(id);
    }

    public List<Oferta> obtenerTodas() {
        return ofertaRepository.obtenerTodas();
    }

    public Oferta obtenerPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return ofertaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada con ID: " + id));
    }

    public List<Oferta> obtenerOfertasVigentes() {
        return ofertaRepository.obtenerTodas().stream()
                .filter(Oferta::esVigente)
                .collect(Collectors.toList());
    }

    public Oferta obtenerOfertaPorHabitacion(Long habitacionId) {
        if (habitacionId == null) {
            return null;
        }
        return ofertaRepository.obtenerTodas().stream()
                .filter(Oferta::esVigente)
                .filter(o -> o.getHabitacionEnOferta() != null && habitacionId.equals(o.getHabitacionEnOferta().getId()))
                .findFirst()
                .orElse(null);
    }

    private void validarOferta(Oferta oferta) {
        if (oferta.getTitulo() == null || oferta.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la oferta es obligatorio");
        }
        if (oferta.getDescuento() <= 0 || oferta.getDescuento() > 100) {
            throw new IllegalArgumentException("El descuento debe ser un porcentaje válido entre 0 y 100");
        }
        if (oferta.getFechaInicio() == null || oferta.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (oferta.getFechaInicio().isAfter(oferta.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}
