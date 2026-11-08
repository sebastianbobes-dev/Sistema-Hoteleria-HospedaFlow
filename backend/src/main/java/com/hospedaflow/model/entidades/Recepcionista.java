package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.RolUsuario;

public class Recepcionista extends Usuario {
    public Recepcionista(){
        super();
        this.setRol(RolUsuario.RECEPCIONISTA);
    }    
    public Recepcionista(long id, String nombre, String correo, String contrasena, String telefono) {
        super(id, nombre, correo, contrasena, telefono, RolUsuario.RECEPCIONISTA);
    }
}
