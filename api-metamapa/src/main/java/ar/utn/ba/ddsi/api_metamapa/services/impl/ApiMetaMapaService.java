
package ar.utn.ba.ddsi.api_metamapa.services.impl;

import ar.utn.ba.ddsi.api_metamapa.dto.HechoFuenteDinamicaDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.entities.TipoNavegacion;
import ar.utn.ba.ddsi.fuente_proxy.services.IMetaMapaService;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho; // <-- Importante para el array
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiMetaMapaService implements IMetaMapaService {

    private final RestTemplate restTemplate = new RestTemplate();
    // La URL base ya incluye /api
    private final String AGREGADOR_API_URL = "http://localhost:8081/api";

    public ApiMetaMapaService() {}

    // ... (otros métodos como obtenerColecciones y obtenerHechos) ...

    @Override
    public Mono<List<HechoDTO>> obtenerHechosPorColeccion(Long id, TipoNavegacion tipo) {
        return Mono.fromCallable(() -> {

            String url = AGREGADOR_API_URL + "/colecciones/" + id + "/hechos";

            // El agregador devuelve objetos de dominio 'Hecho'
            Hecho[] hechosRecibidos = restTemplate.getForObject(url, Hecho[].class);

            if (hechosRecibidos == null) {
                return Collections.emptyList();
            }

            // Mapeamos de Hecho a HechoDTO para la respuesta final de MetaMapa
            return Arrays.stream(hechosRecibidos)
                    .map(hecho -> HechoDTO.toDTO(
                            hecho.getId(),
                            hecho.getTitulo(),
                            hecho.getDescripcion(),
                            hecho.getCategoria(),
                            hecho.getUbicacion().getLatitud(),
                            hecho.getUbicacion().getLongitud()
                    ))
                    .collect(Collectors.toList());
        });
    }


    @Override
    public Mono<List<HechoDTO>> obtenerHechos(Map<String, String> filtros) {
        return Mono.just(Collections.emptyList());
    }

    @Override
    public Mono<List<ColeccionDTO>> obtenerColecciones() {
        // Esta lógica aún necesita que el Agregador devuelva un objeto Coleccion compatible.
        // Por ahora, para que arranque, la dejamos así.
        return Mono.just(Collections.emptyList());
    }

    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudDTO solicitud) {
        return Mono.empty();
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