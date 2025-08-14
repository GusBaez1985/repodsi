package ar.utn.ba.ddsi.fuente_proxy.services.impl;

import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.ColeccionDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuente_proxy.models.dtos.input.SolicitudDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MetaMapaServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private MetaMapaService metaMapaService;

    /*
    @BeforeEach
    void setUp() {
        metaMapaService = new MetaMapaService(webClient);
    }



    @Test
    public void testObtenerColecciones() {
        // Arrange
        ColeccionDTO coleccion1 = ColeccionDTO.toDTO(1L, "Título 1", "Descripción 1");
        ColeccionDTO coleccion2 = ColeccionDTO.toDTO(2L, "Título 2", "Descripción 2");
        List<ColeccionDTO> colecciones = List.of(coleccion1, coleccion2);

        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/colecciones")).thenReturn((WebClient.RequestHeadersSpec) requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ColeccionDTO.class)).thenReturn(Flux.fromIterable(colecciones));

        // Act
        List<ColeccionDTO> resultado = metaMapaService.obtenerColecciones().block();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Título 1", resultado.get(0).getTitulo());
    }


     */
}
