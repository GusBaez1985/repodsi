package ar.utn.ba.ddsi.fuente_proxy.services;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.DesastreDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IApiService {
    public Mono<List<DesastreDTO>> obtenerDesastresPorPagina(int pagina);
    public Mono<DesastreDTO> obtenerDesastrePorId(Long id);
}
