package ar.utn.ba.ddsi.api_metamapa.controllers;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.entities.TipoNavegacion;
import ar.utn.ba.ddsi.fuente_proxy.services.IMetaMapaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metamapa")
public class ApiMetaMapaController {
    private final IMetaMapaService metaMapaService;

    public ApiMetaMapaController(IMetaMapaService metaMapaService) {
        this.metaMapaService = metaMapaService;
    }



    @GetMapping("/hechos")
    Mono<List<HechoDTO>> obtenerHechos(@RequestParam Map<String, String> filtros){
        return metaMapaService.obtenerHechos(filtros);
    }

    @GetMapping("/colecciones")
    Mono<List<ColeccionDTO>> obtenerColecciones(){
        return metaMapaService.obtenerColecciones();
    }

    @GetMapping("/colecciones/{id}/hechos")
    public Mono<List<HechoDTO>> obtenerHechosPorColeccion(
            @PathVariable("id") Long id,
            @RequestParam(name = "navegacion", defaultValue = "CURADA") String navegacion) {

        TipoNavegacion tipo = TipoNavegacion.valueOf(navegacion.toUpperCase());
        return metaMapaService.obtenerHechosPorColeccion(id, tipo);
    }

    @PostMapping("/solicitudes-eliminacion")
    Mono<Void> crearSolicitudEliminacion(@RequestBody SolicitudDTO solicitud){
        return metaMapaService.crearSolicitudEliminacion(solicitud);
    }
}
