package com.hospedaflow.model.entidades;

import com.hospedaflow.model.enums.RolUsuario;
import java.util.List;

public class Gerente extends Usuario {
    public Gerente() {
        super();
        this.setRol(RolUsuario.GERENTE);
    }

    public Gerente(Long id, String nombre, String correo, String contrasena, String telefono) {
        super(id, nombre, correo, contrasena, telefono, RolUsuario.GERENTE);
    }

    @Override
    public List<String> obtenerPermisos() {
        return List.of("GESTIONAR_HABITACIONES", "GESTIONAR_PERSONAL", "GESTIONAR_OFERTAS", "GESTIONAR_RESERVAS");
    }

    @Override
    public String obtenerDescripcionRol() {
        return "Gerente (Administrador del Hotel)";
    }

    @Override
    public boolean puedeGestionarHabitaciones() {
        return true;
    }

    @Override
    public boolean puedeGestionarPersonal() {
        return true;
    }
}
