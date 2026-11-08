package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.util.JsonDataBase;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HabitacionJsonRepository {

    private final String RUTA_ARCHIVO = "src/main/resources/data/habitaciones.json";
    private final JsonDataBase jsonDatabase;

    HabitacionJsonRepository(JsonDataBase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public List<Habitacion> obtenerTodas() {
        return jsonDatabase.leerListaDeJson(RUTA_ARCHIVO, new TypeReference<List<Habitacion>>() {});
    }

    public Optional<Habitacion> buscarPorId(Long id) {
        return obtenerTodas().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
    }

    public void guardar(Habitacion habitacion) {
        List<Habitacion> habitaciones = obtenerTodas();
        
        // Si ya existe, se actualiza; si no, se añade
        habitaciones.removeIf(h -> h.getId().equals(habitacion.getId()));
        habitaciones.add(habitacion);
        
        jsonDatabase.guardarLista(RUTA_ARCHIVO, habitaciones);
    }

    public void eliminar(Long id) {
        List<Habitacion> habitaciones = obtenerTodas();
        habitaciones.removeIf(h -> h.getId().equals(id));
        jsonDatabase.guardarLista(RUTA_ARCHIVO, habitaciones);
    }
}