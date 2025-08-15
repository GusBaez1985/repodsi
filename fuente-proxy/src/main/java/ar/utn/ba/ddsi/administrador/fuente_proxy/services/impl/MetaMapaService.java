package ar.utn.ba.ddsi.administrador.fuente_proxy.services.impl;

import ar.utn.ba.ddsi.administrador.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.administrador.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.administrador.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.administrador.fuente_proxy.models.entities.TipoNavegacion;
import ar.utn.ba.ddsi.administrador.fuente_proxy.services.IMetaMapaService;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class MetaMapaService implements IMetaMapaService {
    private final WebClient webClient;

    public MetaMapaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080/metamapa")
                .build();
    }

    @Override
    public Mono<List<HechoDTO>> obtenerHechos(Map<String, String> filtros) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/hechos");
                    filtros.forEach(builder::queryParam);
                    return builder.build();
                })
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList();
    }

    @Override
    public Mono<List<ColeccionDTO>> obtenerColecciones() {
        return webClient.get()
                .uri("/colecciones")
                .retrieve()
                .bodyToFlux(ColeccionDTO.class)
                .collectList();
    }

    @Override
    public Mono<List<HechoDTO>> obtenerHechosPorColeccion(Long id, TipoNavegacion tipo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/colecciones/{identificador}/hechos")
                        .queryParam("navegacion", tipo.name().toLowerCase()) // "curada" o "irrestricta"
                        .build(id))
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList();
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudDTO solicitud) {
        return webClient.post()
                .uri("/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
