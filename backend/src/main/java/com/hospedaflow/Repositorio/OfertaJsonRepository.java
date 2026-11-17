package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OfertaJsonRepository extends AbstractJsonRepository<Oferta> {

    public OfertaJsonRepository(JsonDataBase jsonDatabase) {
        super(jsonDatabase, "src/main/resources/data/ofertas.json");
    }

    @Override
    protected TypeReference<List<Oferta>> getTypeReference() {
        return new TypeReference<List<Oferta>>() {};
    }
}
