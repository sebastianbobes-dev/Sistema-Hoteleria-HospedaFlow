package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OfertaJsonRepository {

    private final String RUTA_ARCHIVO = "src/main/resources/data/ofertas.json";
    private final JsonDataBase jsonDatabase;

    public OfertaJsonRepository(JsonDataBase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public List<Oferta> obtenerTodas() {
        return jsonDatabase.leerListaDeJson(RUTA_ARCHIVO, new TypeReference<List<Oferta>>() {});
    }

    public Optional<Oferta> buscarPorId(Long id) {
        return obtenerTodas().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public void guardar(Oferta oferta) {
        List<Oferta> ofertas = obtenerTodas();
        ofertas.removeIf(o -> o.getId().equals(oferta.getId()));
        ofertas.add(oferta);
        jsonDatabase.guardarLista(RUTA_ARCHIVO, ofertas);
    }

    public void eliminar(Long id) {
        List<Oferta> ofertas = obtenerTodas();
        ofertas.removeIf(o -> o.getId().equals(id));
        jsonDatabase.guardarLista(RUTA_ARCHIVO, ofertas);
    }
}