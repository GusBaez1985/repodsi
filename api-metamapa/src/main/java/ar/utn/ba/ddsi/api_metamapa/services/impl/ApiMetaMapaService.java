package ar.utn.ba.ddsi.api_metamapa.services.impl;

import ar.utn.ba.ddsi.api_metamapa.dto.SolicitudAgregadorDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.entities.TipoNavegacion;
import ar.utn.ba.ddsi.fuente_proxy.services.IMetaMapaService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class ApiMetaMapaService implements IMetaMapaService {
    private final WebClient webClient;
    private final String AGREGADOR_API_URL = "http://localhost:8081/api";

    public ApiMetaMapaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(AGREGADOR_API_URL).build();
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudDTO solicitud) {
        System.out.println("Enviando solicitud a Agregador (vía WebClient)...");
        SolicitudAgregadorDTO dtoParaAgregador = new SolicitudAgregadorDTO(
                solicitud.getMotivo(),
                solicitud.getIdHecho(),
                solicitud.getIdContribuyente()
        );

        return this.webClient.post()
                .uri("/solicitudes-eliminacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dtoParaAgregador)
                .retrieve()
                // Le decimos a WebClient qué hacer si la respuesta es un error (4xx o 5xx)
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class) // Obtenemos el cuerpo del error como texto
                                .flatMap(errorBody -> {
                                    System.err.println("Error desde Agregador: " + errorBody);
                                    // Creamos y retornamos una excepción clara
                                    return Mono.error(new ResponseStatusException(response.statusCode(), "Error al procesar la solicitud en el Agregador: " + errorBody));
                                })
                )
                .toBodilessEntity()
                .then();
    }
    @Override
    public Mono<List<HechoDTO>> obtenerHechosPorColeccion(Long id, TipoNavegacion tipo) {
        return this.webClient.get()
                .uri("/colecciones/{id}/hechos?navegacion={tipo}", id, tipo.name())
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .collectList();
    }



    @Override
    public Mono<List<HechoDTO>> obtenerHechos(Map<String, String> filtros) {
        return Mono.just(Collections.emptyList());
    }

    @Override
    public Mono<List<ColeccionDTO>> obtenerColecciones() {
        return Mono.just(Collections.emptyList());
    }
}

/*

package ar.utn.ba.ddsi.api_metamapa.services.impl;

// Imports nuevos necesarios para llamar a agregador controller
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Arrays;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.api_metamapa.dto.HechoFuenteDinamicaDTO;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import ar.edu.utn.frba.dds.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.api_metamapa.models.entities.HechoFilter;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.entities.TipoNavegacion;
import ar.utn.ba.ddsi.fuente_proxy.services.IMetaMapaService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ApiMetaMapaService implements IMetaMapaService {

    private IHechoRepository hechoRepository;
    private IColeccionRepository coleccionRepository;
    private ISolicitudEliminacionRepository solicitudEliminacionRepository;

    public ApiMetaMapaService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository, ISolicitudEliminacionRepository solicitudEliminacionRepository) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.solicitudEliminacionRepository = solicitudEliminacionRepository;
    }

    @Override
    public Mono<List<HechoDTO>> obtenerHechos(Map<String, String> filtros) {
        return Mono.fromSupplier(() -> {
            Stream<Hecho> hechosStream = hechoRepository.findAll().stream();

            HechoFilter filtro = new HechoFilter(filtros);
            Stream<Hecho> filtrados = filtro.aplicar(hechosStream);

            return filtrados
                    .map(hecho -> HechoDTO.toDTO(
                            hecho.getId(),
                            hecho.getTitulo(),
                            hecho.getDescripcion(),
                            hecho.getCategoria(),
                            hecho.getUbicacion().getLatitud(),
                            hecho.getUbicacion().getLongitud()
                    ))
                    .toList();
        });
    }

    @Override
    public Mono<List<ColeccionDTO>> obtenerColecciones() {
        List<ColeccionDTO> coleccionDTOS = this.coleccionRepository
                .findAll()
                .stream()
                .map(coleccion -> ColeccionDTO.toDTO(
                        coleccion.getId(),
                        coleccion.getTitulo(),
                        coleccion.getDescripcion(),
                        coleccion.getHechos()
                                .stream()
                                .map(h -> HechoDTO.toDTO(
                                        h.getId(),
                                        h.getTitulo(),
                                        h.getDescripcion(),
                                        h.getCategoria(),
                                        h.getUbicacion().getLatitud(),
                                        h.getUbicacion().getLongitud()
                                ))
                                .collect(Collectors.toList()))
                )
                .toList();

        return Mono.just(coleccionDTOS);
    }

    @Override
    public Mono<List<HechoDTO>> obtenerHechosPorColeccion(Long id, TipoNavegacion tipo) {
        Coleccion coleccion = coleccionRepository.findById(id);

        List<HechoDTO> hechosDTOS;
        if(tipo == TipoNavegacion.CURADA){
            hechosDTOS = coleccion
                    .getHechosCurados()
                    .stream()
                    .map(hecho-> HechoDTO.toDTO(
                            hecho.getId(),
                            hecho.getTitulo(),
                            hecho.getDescripcion(),
                            hecho.getCategoria(),
                            hecho.getUbicacion().getLatitud(),
                            hecho.getUbicacion().getLongitud()
                    ))
                    .toList();
        }
        else{
            hechosDTOS = coleccion
                    .getHechos()
                    .stream()
                    .map(hecho-> HechoDTO.toDTO(
                            hecho.getId(),
                            hecho.getTitulo(),
                            hecho.getDescripcion(),
                            hecho.getCategoria(),
                            hecho.getUbicacion().getLatitud(),
                            hecho.getUbicacion().getLongitud()
                    ))
                    .toList();
        }

        return Mono.just(hechosDTOS);

    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudDTO solicitud) {
        return Mono.fromRunnable(() -> {
            SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(
                    solicitud.getMotivo(),
                    solicitud.getIdHecho(),
                    solicitud.getIdContribuyente()
            );

            solicitudEliminacionRepository.save(solicitudEliminacion);
        });
    }
}
*/