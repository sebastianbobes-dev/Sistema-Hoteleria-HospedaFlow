package com.hospedaflow.controller;

import com.hospedaflow.dto.LoginRequestDTO;
import com.hospedaflow.dto.UsuarioRequestDTO;
import com.hospedaflow.model.entidades.Gerente;
import com.hospedaflow.model.entidades.Huesped;
import com.hospedaflow.model.entidades.Recepcionista;
import com.hospedaflow.model.entidades.Usuario;
import com.hospedaflow.model.enums.RolUsuario;
import com.hospedaflow.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Endpoints de perfiles de usuario. Las contrasenas nunca salen en las respuestas
 * (ver config/JacksonConfig).
 *   GET  /api/usuarios            todos los perfiles
 *   GET  /api/usuarios/{id}       un perfil
 *   POST /api/usuarios            crea un perfil con su rol (201)
 *   PUT  /api/usuarios/{id}/rol   cambia el rol (body: { "rol": "RECEPCIONISTA" })
 *   POST /api/usuarios/login      valida credenciales (200, o 401 si son incorrectas)
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class PerfilRestController {

    private final PerfilService perfilService;

    @Autowired
    public PerfilRestController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(perfilService.consultarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.obtenerPerfil(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioRequestDTO dto) {
        RolUsuario rol = convertirRol(dto.getRol(), RolUsuario.HUESPED);
        Usuario usuario = switch (rol) {
            case GERENTE -> new Gerente();
            case RECEPCIONISTA -> new Recepcionista();
            case HUESPED -> new Huesped();
        };
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getEmail());
        usuario.setContrasena(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.crearPerfilUsuario(usuario));
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> asignarRol(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        RolUsuario rol = convertirRol(dto.getRol(), null);
        return ResponseEntity.ok(perfilService.asignarRol(id, rol));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        Optional<Usuario> autenticado = perfilService.autenticar(dto.getEmail(), dto.getPassword());
        RolUsuario rolPedido = convertirRol(dto.getRol(), null);

        if (autenticado.isEmpty() || (rolPedido != null && autenticado.get().getRol() != rolPedido)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales o rol incorrectos.");
        }
        return ResponseEntity.ok(autenticado.get());
    }

    /** Texto -> enum. Vacio = valor por defecto; valor desconocido = 400. */
    private RolUsuario convertirRol(String texto, RolUsuario porDefecto) {
        if (texto == null || texto.isBlank()) {
            return porDefecto;
        }
        try {
            return RolUsuario.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + texto
                    + " (use GERENTE, RECEPCIONISTA o HUESPED)");
        }
    }
}
