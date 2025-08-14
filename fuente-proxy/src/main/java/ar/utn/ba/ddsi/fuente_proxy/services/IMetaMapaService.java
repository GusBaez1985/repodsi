package ar.utn.ba.ddsi.fuente_proxy.services;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.entities.TipoNavegacion;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IMetaMapaService {
    Mono<List<HechoDTO>> obtenerHechos(Map<String, String> filtros);
    Mono<List<ColeccionDTO>> obtenerColecciones();
    Mono<List<HechoDTO>> obtenerHechosPorColeccion(Long id, TipoNavegacion tipo);
    Mono<Void> crearSolicitudEliminacion(SolicitudDTO solicitud);
}
