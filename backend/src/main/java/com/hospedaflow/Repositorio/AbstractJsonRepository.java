package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Identificable;
import com.hospedaflow.util.JsonDataBase;

import java.util.List;
import java.util.Optional;

/**
 * Clase abstracta base que encapsula la lógica común de persistencia CRUD en archivos JSON
 * para cualquier entidad que implemente Identificable.
 * Aplica los pilares de Abstracción, Herencia, Genéricos y Patrón Template Method.
 */
public abstract class AbstractJsonRepository<T extends Identificable> implements IRepositorio<T, Long> {

    protected final JsonDataBase jsonDatabase;
    protected final String rutaArchivo;

    protected AbstractJsonRepository(JsonDataBase jsonDatabase, String rutaArchivo) {
        this.jsonDatabase = jsonDatabase;
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Método abstracto (Template Method) implementado por cada subclase concreta
     * para proveer su referencia de tipo genérico a Jackson.
     */
    protected abstract TypeReference<List<T>> getTypeReference();

    @Override
    public List<T> obtenerTodas() {
        return jsonDatabase.leerListaDeJson(rutaArchivo, getTypeReference());
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        return obtenerTodas().stream()
                .filter(entidad -> entidad.getId() != null && entidad.getId().equals(id))
                .findFirst();
    }

    @Override
    public void guardar(T entidad) {
        List<T> lista = obtenerTodas();
        lista.removeIf(e -> e.getId() != null && e.getId().equals(entidad.getId()));
        lista.add(entidad);
        jsonDatabase.guardarLista(rutaArchivo, lista);
    }

    @Override
    public void eliminar(Long id) {
        List<T> lista = obtenerTodas();
        lista.removeIf(e -> e.getId() != null && e.getId().equals(id));
        jsonDatabase.guardarLista(rutaArchivo, lista);
    }
}
