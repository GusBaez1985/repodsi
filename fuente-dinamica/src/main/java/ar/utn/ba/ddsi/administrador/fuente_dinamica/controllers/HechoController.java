package ar.utn.ba.ddsi.administrador.fuente_dinamica.controllers;

import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoEdicionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoResponseDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoRequestDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoRevisionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.mappers.HechoMapper;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.services.IFuenteDinamicaService;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {

    @Autowired
    private IFuenteDinamicaService fuenteService;

    @PostMapping
    public String cargarHecho(@RequestBody HechoRequestDTO dto) {
        // Simula fuente registrada
        FuenteContribuyente fuente = new FuenteContribuyente(false); // true = registrada fuente cambiar si requiere comportamiento

        Hecho hecho = Hecho.of(
            dto.titulo,
            dto.descripcion,
            null,
            dto.categoria,
            new Ubicacion(dto.latitud, dto.longitud),
            LocalDate.parse(dto.fechaAcontecimiento),
            fuente
        );

        fuenteService.agregarHecho(hecho);
        return "Hecho cargado con éxito";
    }

    // editamos camppos
    @PutMapping("/{id}")
    public ResponseEntity<String> editarHecho(@PathVariable("id") Long id, @RequestBody HechoEdicionDTO dto) {
        try {
            fuenteService.editarHecho(id, dto);
            return ResponseEntity.ok("Hecho editado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/revision")
    public ResponseEntity<String> revisarHecho(@PathVariable("id") Long id, @RequestBody HechoRevisionDTO dto) {

        try {
            fuenteService.revisarHecho(id, dto);
            return ResponseEntity.ok("Hecho revisado con estado: " + dto.getEstado());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Probar si cargo ok el hecho
    @GetMapping
    public List<HechoResponseDTO> listarHechos() {
        return HechoMapper.toDtoList(fuenteService.obtenerTodosLosHechos());
    }
}