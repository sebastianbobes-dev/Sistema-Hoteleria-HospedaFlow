package com.hospedaflow.Repositorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hospedaflow.model.entidades.Reserva;
import com.hospedaflow.util.JsonDataBase;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservaJsonRepository extends AbstractJsonRepository<Reserva> {

    public ReservaJsonRepository(JsonDataBase jsonDatabase) {
        super(jsonDatabase, "src/main/resources/data/reservas.json");
    }

    @Override
    protected TypeReference<List<Reserva>> getTypeReference() {
        return new TypeReference<List<Reserva>>() {};
    }

    public void actualizar(Reserva reserva) {
        guardar(reserva);
    }
}
