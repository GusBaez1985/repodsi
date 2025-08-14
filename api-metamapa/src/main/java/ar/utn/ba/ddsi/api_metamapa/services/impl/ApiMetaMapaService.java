package ar.utn.ba.ddsi.api_metamapa.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
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
