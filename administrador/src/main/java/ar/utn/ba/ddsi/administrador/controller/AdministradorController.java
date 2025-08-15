
package ar.utn.ba.ddsi.administrador.controller;

import ar.utn.ba.ddsi.administrador.dto.ColeccionDTO;
import ar.utn.ba.ddsi.administrador.dto.ColeccionResponseDTO;
import ar.utn.ba.ddsi.administrador.dto.HechoResponseDTO;
import ar.utn.ba.ddsi.administrador.dto.ModificarAlgoritmoDTO;
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
    public ColeccionResponseDTO modificarColeccion(@PathVariable Long id, @RequestBody ColeccionDTO dto) {
        return ColeccionResponseDTO.from(administradorService.modificarColeccion(id, dto));
    }

    @DeleteMapping("/{id}")
    public void eliminarColeccion(@PathVariable Long id) {
        administradorService.eliminarColeccion(id);
    }


    @GetMapping("/{id}/hechos")
    public List<HechoResponseDTO> obtenerHechos(@PathVariable Long id) {
        return administradorService.obtenerHechosDeColeccion(id)
                .stream()
                .map(HechoResponseDTO::from)
                .toList();
    }
    @PutMapping("/{id}/algoritmo")
    public ResponseEntity<String> cambiarAlgoritmo(
            @PathVariable Long id,
            @RequestBody ModificarAlgoritmoDTO dto
    ) {
        administradorService.modificarAlgoritmoDeConsenso(id, dto.getTipo());
        return ResponseEntity.ok("Algoritmo de consenso modificado");
    }


}

