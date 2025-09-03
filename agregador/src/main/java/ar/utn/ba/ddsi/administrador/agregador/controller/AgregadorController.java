package ar.utn.ba.ddsi.administrador.agregador.controller;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.administrador.agregador.dto.ColeccionRequestDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO;
import ar.utn.ba.ddsi.administrador.service.factory.FuenteFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgregadorController {

    private final IColeccionRepository coleccionRepository;

    public AgregadorController(IColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    @GetMapping("/colecciones")
    public List<Coleccion> listarColecciones() {
        return coleccionRepository.findAll();
    }

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    @PostMapping("/colecciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Coleccion crearColeccion(@RequestBody ColeccionRequestDTO dto) {
        Coleccion nuevaColeccion = new Coleccion(dto.getTitulo(), dto.getDescripcion(), null);
        // Lógica futura para el algoritmo...
        coleccionRepository.save(nuevaColeccion);
        return nuevaColeccion;
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Coleccion> obtenerColeccion(@PathVariable Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(coleccion);
    }



    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            System.out.println("[AGREGADOR DEBUG] Colección no encontrada con ID: " + id);
            return ResponseEntity.notFound().build();
        }

        // Usamos la nueva clase Factory con el método correcto "crear"
        Fuente nuevaFuente = FuenteFactory.crear(dto);

        coleccion.agregarFuente(nuevaFuente);

        // Guardamos la colección con su nueva fuente asociada
        coleccionRepository.save(coleccion);

        System.out.println("[AGREGADOR DEBUG] Fuente '" + dto.getNombre() + "' de tipo '" + dto.getTipo() + "' fue agregada y guardada en Colección ID " + id);

        return ResponseEntity.ok().build();
    }

    /*
    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            System.out.println("[AGREGADOR DEBUG] Colección no encontrada con ID: " + id);
            return ResponseEntity.notFound().build();
        }



        System.out.println("[AGREGADOR DEBUG] Se agregaría una fuente de tipo '" + dto.getTipo() + "' a la colección " + id);
        // Por ahora, solo simulamos que la agregamos para que el endpoint funcione.
        // coleccionRepository.save(coleccion); // Descomentar cuando la lógica de la fuente esté lista.

        return ResponseEntity.ok().build();
    }
    */
    @GetMapping("/colecciones/{id}/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechosDeColeccion(@PathVariable("id") Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(coleccion.getHechos());
    }

}