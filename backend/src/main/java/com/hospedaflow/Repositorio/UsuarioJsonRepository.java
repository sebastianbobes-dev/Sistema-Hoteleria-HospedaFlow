package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Usuario;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioJsonRepository extends AbstractJsonRepository<Usuario> {

    public UsuarioJsonRepository(JsonDataBase jsonDatabase) {
        super(jsonDatabase, "src/main/resources/data/usuarios.json");
    }

    @Override
    protected TypeReference<List<Usuario>> getTypeReference() {
        return new TypeReference<List<Usuario>>() {};
    }
}
