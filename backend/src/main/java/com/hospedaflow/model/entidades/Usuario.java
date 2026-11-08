package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.RolUsuario;

public abstract class Usuario {
    private long id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private RolUsuario rol;

    public Usuario(){}

    public Usuario(long id, String nombre, String correo, String contrasena, String telefono, RolUsuario rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.rol = rol;
    }

    public boolean autenticar(String constrasenaIngresada){
        return this.contrasena != null && this.contrasena.equals(constrasenaIngresada);
    }

    //getter y setter
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public RolUsuario getRol() {
        return rol;
    }
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
    


    
}
