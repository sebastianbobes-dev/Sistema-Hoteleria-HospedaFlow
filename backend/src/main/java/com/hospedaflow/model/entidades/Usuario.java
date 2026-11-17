package com.hospedaflow.model.entidades;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hospedaflow.model.enums.RolUsuario;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "rol", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Gerente.class, name = "GERENTE"),
    @JsonSubTypes.Type(value = Recepcionista.class, name = "RECEPCIONISTA"),
    @JsonSubTypes.Type(value = Huesped.class, name = "HUESPED")
})
public abstract class Usuario implements Identificable {
    private Long id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private RolUsuario rol;

    public Usuario(){}

    public Usuario(Long id, String nombre, String correo, String contrasena, String telefono, RolUsuario rol) {
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

    // Métodos abstractos polimórficos implementados por cada subclase (Gerente, Recepcionista, Huesped)
    @JsonIgnore
    public abstract List<String> obtenerPermisos();

    @JsonIgnore
    public abstract String obtenerDescripcionRol();

    @JsonIgnore
    public abstract boolean puedeGestionarHabitaciones();

    @JsonIgnore
    public abstract boolean puedeGestionarPersonal();

    //getter y setter
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
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
    public String getEmail() {
        return correo;
    }
    public void setEmail(String email) {
        this.correo = email;
    }

    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getPassword() {
        return contrasena;
    }
    public void setPassword(String password) {
        this.contrasena = password;
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
