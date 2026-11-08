package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.RolUsuario;

public class Huesped extends Usuario {
    public Huesped() {
        super();
        setRol(RolUsuario.HUESPED);
    }

    public Huesped(Long id, String nombre, String email, String password, String telefono) {
        super(id, nombre, email, password, telefono, RolUsuario.HUESPED);
    }
}