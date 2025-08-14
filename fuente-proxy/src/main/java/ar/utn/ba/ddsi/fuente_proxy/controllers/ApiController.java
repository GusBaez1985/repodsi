package ar.utn.ba.ddsi.fuente_proxy.controllers;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.DesastreDTO;
import ar.utn.ba.ddsi.fuente_proxy.services.IApiService;
import ar.utn.ba.ddsi.fuente_proxy.services.impl.ApiService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

//@RestController
//@RequestMapping("/api/desastres")
public class ApiController {
    private final IApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping
    public Mono<List<DesastreDTO>> obtenerDesastresPorPagina(@RequestParam(defaultValue = "1") int page) {
        return apiService.obtenerDesastresPorPagina(page);
    }

    @GetMapping("/{id}")
    public Mono<DesastreDTO> obtenerDesastrePorId(@PathVariable Long id){
        return apiService.obtenerDesastrePorId(id);
    };
}
