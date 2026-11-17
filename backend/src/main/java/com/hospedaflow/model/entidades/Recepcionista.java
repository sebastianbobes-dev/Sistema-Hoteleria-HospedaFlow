package com.hospedaflow.model.entidades;

import com.hospedaflow.model.enums.RolUsuario;
import java.util.List;

public class Recepcionista extends Usuario {
    public Recepcionista() {
        super();
        this.setRol(RolUsuario.RECEPCIONISTA);
    }

    public Recepcionista(Long id, String nombre, String correo, String contrasena, String telefono) {
        super(id, nombre, correo, contrasena, telefono, RolUsuario.RECEPCIONISTA);
    }

    @Override
    public List<String> obtenerPermisos() {
        return List.of("CONSULTAR_HABITACIONES", "GESTIONAR_RESERVAS", "CONSULTAR_OFERTAS");
    }

    @Override
    public String obtenerDescripcionRol() {
        return "Recepcionista de Turno";
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
