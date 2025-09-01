package ar.utn.ba.ddsi.administrador.agregador.controller;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/")
public class AgregadorController {

    private final IColeccionRepository coleccionRepository;

    public AgregadorController(IColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    @GetMapping("/colecciones")
    public List<Coleccion> listarColecciones() {
        return coleccionRepository.findAll();
    }

    @PostMapping("/colecciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Coleccion crearColeccion(@RequestBody Coleccion coleccion) {
        coleccionRepository.save(coleccion);
        return coleccion;
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Coleccion> obtenerColeccion(@PathVariable Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(coleccion);
    }
}