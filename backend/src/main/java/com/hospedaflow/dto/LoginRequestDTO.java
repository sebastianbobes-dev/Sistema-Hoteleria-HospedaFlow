package com.hospedaflow.dto;

/**
 * DTO plano para iniciar sesion. "rol" es opcional: si se envia, debe coincidir
 * con el rol real del usuario.
 */
public class LoginRequestDTO {
    private String email;
    private String password;
    private String rol;

    public LoginRequestDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
