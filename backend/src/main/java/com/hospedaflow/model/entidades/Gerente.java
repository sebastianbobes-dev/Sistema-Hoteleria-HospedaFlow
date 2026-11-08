package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.RolUsuario;

public class Gerente extends Usuario {
    public Gerente() {
        super();
        this.setRol(RolUsuario.GERENTE);
    }

    public Gerente(long id, String nombre, String correo, String contrasena, String telefono) {
        super(id, nombre, correo, contrasena, telefono, RolUsuario.GERENTE);
    }
    
    
}
