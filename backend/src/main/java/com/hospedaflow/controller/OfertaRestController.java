package com.hospedaflow.controller;

import com.hospedaflow.dto.OfertaRequestDTO;
import com.hospedaflow.model.entidades.Habitacion;
import com.hospedaflow.model.entidades.Oferta;
import com.hospedaflow.service.HabitacionService;
import com.hospedaflow.service.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints de ofertas. Delega toda la logica de vigencia y validacion en OfertaService.
 *   GET    /api/ofertas            todas
 *   GET    /api/ofertas/vigentes   solo las vigentes hoy
 *   GET    /api/ofertas/{id}       una oferta
 *   POST   /api/ofertas            crea (201)
 *   PUT    /api/ofertas/{id}       modifica
 *   DELETE /api/ofertas/{id}       elimina
 */
@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = "*")
public class OfertaRestController {

    private final OfertaService ofertaService;
    private final HabitacionService habitacionService;

    @Autowired
    public OfertaRestController(OfertaService ofertaService, HabitacionService habitacionService) {
        this.ofertaService = ofertaService;
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public ResponseEntity<List<Oferta>> listar() {
        return ResponseEntity.ok(ofertaService.obtenerTodas());
    }

    @GetMapping("/vigentes")
    public ResponseEntity<List<Oferta>> listarVigentes() {
        return ResponseEntity.ok(ofertaService.obtenerOfertasVigentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oferta> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Oferta> crear(@RequestBody OfertaRequestDTO dto) {
        Oferta oferta = convertir(new Oferta(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ofertaService.crearOferta(oferta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oferta> modificar(@PathVariable Long id, @RequestBody OfertaRequestDTO dto) {
        Oferta oferta = convertir(new Oferta(), dto);
        oferta.setId(id);
        return ResponseEntity.ok(ofertaService.modificarOferta(oferta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        ofertaService.eliminarOferta(id);
        return ResponseEntity.ok(Map.of("mensaje", "Oferta eliminada correctamente"));
    }

    /** DTO -> entidad. La habitacion se resuelve por id (si no existe, el servicio responde 400). */
    private Oferta convertir(Oferta oferta, OfertaRequestDTO dto) {
        oferta.setTitulo(dto.getTitulo());
        oferta.setDescripcion(dto.getDescripcion());
        oferta.setDescuento(dto.getPorcentajeDescuento());
        oferta.setFechaInicio(dto.getFechaInicio());
        oferta.setFechaFin(dto.getFechaFin());
        oferta.setActiva(dto.getActiva() == null || dto.getActiva());
        Habitacion habitacion = dto.getHabitacionId() != null
                ? habitacionService.obtenerPorId(dto.getHabitacionId())
                : null;
        oferta.setHabitacionEnOferta(habitacion);
        return oferta;
    }
}
