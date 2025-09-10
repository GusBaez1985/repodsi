package ar.utn.ba.ddsi.fuente_proxy.services.impl;

import ar.edu.utn.frba.dds.models.entities.adapter.IAdapterAPI;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.DesastreDTO;
import ar.utn.ba.ddsi.fuente_proxy.services.IApiService;

import java.util.List;

public class ApiAdapter implements IAdapterAPI {
    private final IApiService apiService;

    public ApiAdapter(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public List<Hecho> obtenerHechos() {
        return apiService.obtenerTodosLosDesastres()
                .stream()
                .map(this::mapearADominio)
                .toList();
    }

    private Hecho mapearADominio(DesastreDTO dto) {
        return Hecho.of(
                dto.getTitulo(),
                dto.getDescripcion(),
                TipoHecho.TEXTO,
                dto.getCategoria(),
                new Ubicacion(dto.getLatitud(), dto.getLongitud()),
                dto.getCreated_at().toLocalDateTime(),
                new FuenteCargaManual("Api Cátedra"));
    }
}
