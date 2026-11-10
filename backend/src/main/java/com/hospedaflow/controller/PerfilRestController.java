package com.hospedaflow.controller;

import com.hospedaflow.dto.UsuarioRequestDTO;
import com.hospedaflow.model.entity.Usuario;
import com.hospedaflow.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class PerfilRestController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(perfilService.consultarPerfiles());
    }

    @PostMapping
    public ResponseEntity<?> crearPerfil(@RequestBody UsuarioRequestDTO dto) {
        try {
            Usuario usuario = perfilService.crearPerfilGeneral(
                dto.getNombre(), dto.getEmail(), dto.getPassword(), dto.getTelefono(), dto.getRol()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
