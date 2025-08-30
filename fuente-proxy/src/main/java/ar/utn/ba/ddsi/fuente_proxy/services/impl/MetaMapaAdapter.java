package ar.utn.ba.ddsi.fuente_proxy.services.impl;

import ar.edu.utn.frba.dds.models.entities.adapter.IAdapterAPI;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.services.IMetaMapaService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class MetaMapaAdapter implements IAdapterAPI {
    private final IMetaMapaService metaMapaService;

    public MetaMapaAdapter(IMetaMapaService metaMapaService) {
        this.metaMapaService = metaMapaService;
    }

    @Override
    public List<Hecho> obtenerHechos() {
        return metaMapaService.obtenerHechos(Map.of())
                .map(lista -> lista.stream()
                        .map(this::mapearADominio)
                        .toList()
                )
                .block();
    }

    private Hecho mapearADominio(HechoDTO dto) {
        return Hecho.of(
                dto.getTitulo(),
                dto.getDescripcion(),
                TipoHecho.TEXTO,
                dto.getCategoria(),
                new Ubicacion(dto.getLatitud(), dto.getLongitud()),
                dto.getFecAcontecimiento(),
                new FuenteCargaManual("Api MetaMapa"));
    }
}
