package ar.utn.ba.ddsi.fuente_proxy.services.impl;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.DesastreDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.DesastresResponseDTO;
import ar.utn.ba.ddsi.fuente_proxy.services.IApiService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService implements IApiService {
    private WebClient webClient;
    private Integer cantidadPaginas;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api-ddsi.disilab.ar")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer rY3j0CD1b4hpJBNWwZvJkva2NhsGEukeS2pFQkjE2yMBmk6sdlGQ5ATQkpYo")
                .build();

        this.cantidadPaginas = obtenerCantidadPaginas();
    }

    private Integer obtenerCantidadPaginas() {
        DesastresResponseDTO respuesta = webClient.get()
                .uri("/api/desastres?page=1")
                .retrieve()
                .bodyToMono(DesastresResponseDTO.class)
                .block();

        return respuesta != null && respuesta.getLast_page() != null
                ? respuesta.getLast_page()
                : 1;
    }

    @Override
    public Mono<List<DesastreDTO>> obtenerDesastresPorPagina(Integer page) {
        return webClient.get()
                .uri("/api/desastres?page=" + page)
                .retrieve()
                .bodyToMono(DesastresResponseDTO.class)
                .map(DesastresResponseDTO::getData);
    }

    @Override
    public List<DesastreDTO> obtenerTodosLosDesastres() {
        List<DesastreDTO> desastres = new ArrayList<>();
        for (int i = 1; i <= cantidadPaginas; i++) {
            List<DesastreDTO> pagina = obtenerDesastresPorPagina(i).block();
            if (pagina != null) {
                desastres.addAll(pagina);
            }
        }
        return desastres;
    }

    @Override
    public Mono<DesastreDTO> obtenerDesastrePorId(Long id) {
        return webClient
                .get()
                .uri("/api/desastres/{id}", id)
                .retrieve()
                .bodyToMono(DesastreDTO.class);
    }
}
