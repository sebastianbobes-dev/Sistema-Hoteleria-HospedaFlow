package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Usuario;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioJsonRepository {

    private final String RUTA_ARCHIVO = "src/main/resources/data/usuarios.json";
    private final JsonDataBase jsonDatabase;

    public UsuarioJsonRepository(JsonDataBase jsonDatabase) {
        this.jsonDatabase = jsonDatabase;
    }

    public List<Usuario> obtenerTodas() {
        return jsonDatabase.leerListaDeJson(RUTA_ARCHIVO, new TypeReference<List<Usuario>>() {});
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return obtenerTodas().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public void guardar(Usuario usuario) {
        List<Usuario> usuarios = obtenerTodas();
        usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
        usuarios.add(usuario);
        jsonDatabase.guardarLista(RUTA_ARCHIVO, usuarios);
    }

    public void eliminar(Long id) {
        List<Usuario> usuarios = obtenerTodas();
        usuarios.removeIf(u -> u.getId().equals(id));
        jsonDatabase.guardarLista(RUTA_ARCHIVO, usuarios);
    }
}