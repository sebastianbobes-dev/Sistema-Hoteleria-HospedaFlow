package com.hospedaflow.service;

import com.hospedaflow.model.entidades.Gerente;
import com.hospedaflow.model.entidades.Huesped;
import com.hospedaflow.model.entidades.Recepcionista;
import com.hospedaflow.model.entidades.Usuario;
import com.hospedaflow.model.enums.RolUsuario;
import com.hospedaflow.Repositorio.UsuarioJsonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilService {

    private final UsuarioJsonRepository usuarioRepository;

    public PerfilService(UsuarioJsonRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearPerfilUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es obligatorio");
        }

        // Validación de correo duplicado
        boolean correoExistente = usuarioRepository.obtenerTodas().stream()
                .anyMatch(u -> u.getCorreo().equalsIgnoreCase(usuario.getCorreo().trim())
                        && (usuario.getId() == null || !u.getId().equals(usuario.getId())));
        if (correoExistente) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo: " + usuario.getCorreo());
        }

        // Asignación de ID autoincremental si no tiene
        if (usuario.getId() == null) {
            long maxId = usuarioRepository.obtenerTodas().stream()
                    .mapToLong(u -> u.getId() != null ? u.getId() : 0L)
                    .max()
                    .orElse(0L);
            usuario.setId(maxId + 1);
        }

        usuarioRepository.guardar(usuario);
        return usuario;
    }

    public Huesped crearPerfilHuesped(Huesped huesped) {
        if (huesped == null) {
            throw new IllegalArgumentException("El perfil de huésped no puede ser nulo");
        }
        huesped.setRol(RolUsuario.HUESPED);
        return (Huesped) crearPerfilUsuario(huesped);
    }

    public Usuario obtenerPerfil(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
    }

    public List<Usuario> consultarUsuarios() {
        return usuarioRepository.obtenerTodas();
    }

    public Usuario asignarRol(Long idUsuario, RolUsuario nuevoRol) {
        if (idUsuario == null || nuevoRol == null) {
            throw new IllegalArgumentException("El ID de usuario y el nuevo rol son obligatorios");
        }
        Usuario usuario = obtenerPerfil(idUsuario);

        Usuario usuarioActualizado;
        switch (nuevoRol) {
            case GERENTE -> usuarioActualizado = new Gerente(
                    usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getContrasena(), usuario.getTelefono());
            case RECEPCIONISTA -> usuarioActualizado = new Recepcionista(
                    usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getContrasena(), usuario.getTelefono());
            case HUESPED -> usuarioActualizado = new Huesped(
                    usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getContrasena(), usuario.getTelefono());
            default -> {
                usuario.setRol(nuevoRol);
                usuarioActualizado = usuario;
            }
        }

        usuarioRepository.guardar(usuarioActualizado);
        return usuarioActualizado;
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) {
        if (correo == null || contrasena == null) {
            return Optional.empty();
        }
        return usuarioRepository.obtenerTodas().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo.trim()) && u.autenticar(contrasena))
                .findFirst();
    }

    public void eliminarPerfil(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        obtenerPerfil(id); // Valida que exista
        usuarioRepository.eliminar(id);
    }
}

