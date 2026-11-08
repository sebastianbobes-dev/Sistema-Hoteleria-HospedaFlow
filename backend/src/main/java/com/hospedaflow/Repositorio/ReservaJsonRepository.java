package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Reserva;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservaJsonRepository {

    private final String RUTA_ARCHIVO = "src/main/resources/data/reservas.json";

    private final JsonDataBase jsonDatabase;

    ReservaJsonRepository(JsonDataBase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public List<Reserva> obtenerTodas() {
        return jsonDatabase.leerListaDeJson(RUTA_ARCHIVO, new TypeReference<List<Reserva>>() {});
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return obtenerTodas().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public void guardar(Reserva reserva) {
        List<Reserva> reservas = obtenerTodas();
        reservas.removeIf(r -> r.getId().equals(reserva.getId()));
        reservas.add(reserva);
        jsonDatabase.guardarLista(RUTA_ARCHIVO, reservas);
    }

    public void actualizar(Reserva reserva) {
        guardar(reserva); // Reutiliza la lógica de reemplazo
    }
}