
package ar.utn.ba.ddsi.administrador.controller;

import ar.utn.ba.ddsi.administrador.dto.*;
import ar.utn.ba.ddsi.administrador.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/colecciones")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }
    @GetMapping
    public List<ColeccionResponseDTO> listarColecciones() {
        return administradorService.obtenerTodasLasColecciones();
    }

    @PostMapping
    public ColeccionResponseDTO crearColeccion(@RequestBody ColeccionDTO dto) {
        return ColeccionResponseDTO.from(administradorService.crearColeccion(dto));
    }

    @PutMapping("/{id}")
    public ColeccionResponseDTO modificarColeccion(@PathVariable("id") Long id, @RequestBody ColeccionDTO dto) {
        return ColeccionResponseDTO.from(administradorService.modificarColeccion(id, dto));
    }

    @DeleteMapping("/{id}")
    public void eliminarColeccion(@PathVariable("id") Long id) {
        administradorService.eliminarColeccion(id);
    }


    @GetMapping("/{id}/hechos")
    public List<HechoResponseDTO> obtenerHechos(@PathVariable("id") Long id) {
        return administradorService.obtenerHechosDeColeccion(id)
                .stream()
                .map(HechoResponseDTO::from)
                .toList();
    }

    @PutMapping("/{id}/algoritmo")
    public ResponseEntity<String> cambiarAlgoritmo(
            @PathVariable("id") Long id, // <-- SE AÑADE @PathVariable("id")
            @RequestBody ModificarAlgoritmoDTO dto
    ) {
        administradorService.modificarAlgoritmoDeConsenso(id, dto.getTipo());
        return ResponseEntity.ok("Algoritmo de consenso modificado");
    }

    @PostMapping("/{id}/fuentes")
    public ResponseEntity<String> agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteDTO dto) {
        administradorService.agregarFuente(id, dto);
        return ResponseEntity.ok("Fuente agregada correctamente a la colección.");
    }

    @DeleteMapping("/{id}/fuentes/{idFuente}")
    public ResponseEntity<String> quitarFuenteDeColeccion(
            @PathVariable("id") Long id,               // <-- SE AÑADE @PathVariable("id")
            @PathVariable("idFuente") Long idFuente    // <-- SE AÑADE @PathVariable("idFuente")
    ) {
        administradorService.quitarFuente(id, idFuente);
        return ResponseEntity.ok("Fuente quitada correctamente de la colección.");
    }
}
