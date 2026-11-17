package com.hospedaflow.model.entidades;

import com.hospedaflow.model.enums.RolUsuario;
import java.util.List;

public class Huesped extends Usuario {
    public Huesped() {
        super();
        setRol(RolUsuario.HUESPED);
    }

    public Huesped(Long id, String nombre, String email, String password, String telefono) {
        super(id, nombre, email, password, telefono, RolUsuario.HUESPED);
    }

    @Override
    public List<String> obtenerPermisos() {
        return List.of("REGISTRAR_RESERVA_PROPIA", "CONSULTAR_OFERTAS_VIGENTES");
    }

    @Override
    public String obtenerDescripcionRol() {
        return "Huésped / Cliente";
    }

    @Override
    public boolean puedeGestionarHabitaciones() {
        return false;
    }

    @Override
    public boolean puedeGestionarPersonal() {
        return false;
    }
}
