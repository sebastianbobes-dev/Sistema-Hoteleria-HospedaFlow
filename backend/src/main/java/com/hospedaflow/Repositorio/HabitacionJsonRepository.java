package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HabitacionJsonRepository extends AbstractJsonRepository<Habitacion> {

    public HabitacionJsonRepository(JsonDataBase jsonDatabase) {
        super(jsonDatabase, "src/main/resources/data/habitaciones.json");
    }

    @Override
    protected TypeReference<List<Habitacion>> getTypeReference() {
        return new TypeReference<List<Habitacion>>() {};
    }
}
