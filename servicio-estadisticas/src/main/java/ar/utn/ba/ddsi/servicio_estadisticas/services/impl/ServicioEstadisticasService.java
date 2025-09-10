package ar.utn.ba.ddsi.servicio_estadisticas.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.utn.ba.ddsi.servicio_estadisticas.models.dtos.input.NominatimResponse;
import ar.utn.ba.ddsi.servicio_estadisticas.models.entities.Estadistica;
import ar.utn.ba.ddsi.servicio_estadisticas.models.repositories.IEstadisticasRepository;
import ar.utn.ba.ddsi.servicio_estadisticas.services.IServicioEstadisticasService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ServicioEstadisticasService implements IServicioEstadisticasService {

    private final WebClient webClientAgregador;
    private final WebClient webClientNominatim;
    private final Map<String, String> cacheUbicacionProvincia;
    private final IEstadisticasRepository estadisticaRepository;

    public ServicioEstadisticasService(WebClient.Builder webClientBuilder, IEstadisticasRepository estadisticaRepository) {
        this.webClientAgregador = webClientBuilder
                .baseUrl("http://localhost:8081/api")
                .build();
        this.webClientNominatim = webClientBuilder
                .baseUrl("https://nominatim.openstreetmap.org")
                .build();
        this.cacheUbicacionProvincia = new HashMap<>();
        this.estadisticaRepository = estadisticaRepository;
    }

    @Override
    public String obtenerProvinciaConMasReportes(Long idColeccion) {
        List<Hecho> hechos = webClientAgregador.get()
                .uri("/colecciones/{id}/hechos", idColeccion)
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                    // Podés loguear o devolver un Mono vacío
                    return Mono.empty();
                })
                .bodyToFlux(Hecho.class)
                .collectList()
                .block();

        if (hechos.isEmpty()) {return "No existe la colección";}

        Map<String, Integer> contadorPorProvincia = new HashMap<>();

        for (Hecho hecho : hechos) {
            Ubicacion ubicacion = hecho.getUbicacion();
            String provincia = obtenerProvinciaDesdeUbicacion(ubicacion);

            if (provincia != null) {
                if (contadorPorProvincia.containsKey(provincia)) {
                    contadorPorProvincia.put(provincia, contadorPorProvincia.get(provincia) + 1);
                } else {
                    contadorPorProvincia.put(provincia, 1);
                }
            }
        }

        return contadorPorProvincia.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String obtenerProvinciaDesdeUbicacion(Ubicacion ubicacion) {
        String key = ubicacion.getLatitud() + "," + ubicacion.getLongitud();

        if (cacheUbicacionProvincia.containsKey(key)) {
            return cacheUbicacionProvincia.get(key);
        }

        String url = String.format(
                "/reverse?lat=%s&lon=%s&format=json",
                ubicacion.getLatitud(),
                ubicacion.getLongitud()
        );

        NominatimResponse response = webClientNominatim.get()
                .uri(url)
                .header("User-Agent", "ServicioEstadisticas (ggg@gmail.com)")
                .retrieve()
                .bodyToMono(NominatimResponse.class)
                .block();

        String provincia = (response != null && response.getAddress() != null)
                ? response.getAddress().getState()
                : null;

        cacheUbicacionProvincia.put(key, provincia);

        return provincia;
    }

    @Override
    public String obtenerCategoriaConMasReportes() {
        return estadisticaRepository.findTopByTipoOrderByValorDesc("categoria")
                .map(Estadistica::getClave)
                .orElse(null);
    }

    @Override
    public String obtenerProvinciaConMasReportesSegunCategoria(String categoria) {
        return estadisticaRepository.findTopByTipoAndClaveOrderByValorDesc("provincia", categoria)
                .map(Estadistica::getClave)
                .orElse(null);
    }

    @Override
    public Integer obtenerHoraDondeOcurrenMasReportesSegunCategoria(String categoria) {
        return estadisticaRepository.findTopByTipoAndClaveOrderByValorDesc("hora", categoria)
                .map(e -> Integer.valueOf(e.getClave()))
                .orElse(null);
    }

    @Override
    public Integer obtenerCantidadDeSolicitudesSpam() {
        return estadisticaRepository.findTopByTipoOrderByValorDesc("spam")
                .map(e -> e.getValor().intValue())
                .orElse(0);
    }

    @Override
    public String exportarEstadisticasCSV(String ruta) {
        List<Estadistica> estadisticas = estadisticaRepository.findAll();

        try (PrintWriter writer = new PrintWriter(new File(ruta))) {
            writer.println("tipo,clave,valor,fecha");

            for (Estadistica e : estadisticas) {
                writer.printf("%s,%s,%d,%s%n",
                        e.getTipo(),
                        e.getClave(),
                        e.getValor(),
                        e.getFechaCalculo());
            }
            return ruta;
        } catch (IOException e) {
            throw new RuntimeException("Error al exportar CSV", e);
        }
    }

    public List<Estadistica> calcularEstadisticas() {
        List<Hecho> hechos = webClientAgregador.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(Hecho.class)
                .collectList()
                .block();

        List<SolicitudEliminacion> solicitudesSpam = webClientAgregador.get()
                .uri("/solicitudes_spam")
                .retrieve()
                .bodyToFlux(SolicitudEliminacion.class)
                .collectList()
                .block();

        List<Estadistica> resultado = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        //Categoría con más reportes
        Map<String, Long> porCategoria = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));

        porCategoria.forEach((cat, count) -> {
            Estadistica e = new Estadistica();
            e.setTipo("categoria");
            e.setClave(cat);
            e.setValor(count);
            e.setFechaCalculo(hoy);
            resultado.add(e);
        });

        //Provincia con más reportes
        Map<String, Long> porProvincia = hechos.stream()
                .map(Hecho::getUbicacion)
                .map(this::obtenerProvinciaDesdeUbicacion)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        porProvincia.forEach((prov, count) -> {
            Estadistica e = new Estadistica();
            e.setTipo("provincia");
            e.setClave(prov);
            e.setValor(count);
            e.setFechaCalculo(hoy);
            resultado.add(e);
        });

        //Provincia con más reportes según categoría
        Map<String, Map<String, Long>> porCategoriaYProvincia = hechos.stream()
                .collect(Collectors.groupingBy(
                        Hecho::getCategoria,
                        Collectors.groupingBy(
                                h -> obtenerProvinciaDesdeUbicacion(h.getUbicacion()),
                                Collectors.counting()
                        )
                ));

        porCategoriaYProvincia.forEach((categoria, mapProv) -> {
            mapProv.forEach((prov, count) -> {
                if (prov != null) {
                    Estadistica e = new Estadistica();
                    e.setTipo("categoria-provincia");
                    e.setClave(categoria + ":" + prov);
                    e.setValor(count);
                    e.setFechaCalculo(hoy);
                    resultado.add(e);
                }
            });
        });

        //Hora del día con más reportes según categoría
        Map<String, Map<Integer, Long>> porCategoriaYHora = hechos.stream()
                .collect(Collectors.groupingBy(
                        Hecho::getCategoria,
                        Collectors.groupingBy(
                                h -> h.getFecAcontecimiento().getHour(),
                                Collectors.counting()
                        )
                ));

        porCategoriaYHora.forEach((categoria, mapaHora) -> {
            mapaHora.forEach((hora, count) -> {
                Estadistica e = new Estadistica();
                e.setTipo("categoria-hora");
                e.setClave(categoria + ":" + hora);
                e.setValor(count);
                e.setFechaCalculo(hoy);
                resultado.add(e);
            });
        });

        //Solicitudes de eliminación que son spam
        long cantidadSpam = solicitudesSpam.size();

        Estadistica eSpam = new Estadistica();
        eSpam.setTipo("solicitud");
        eSpam.setClave("spam");
        eSpam.setValor(cantidadSpam);
        eSpam.setFechaCalculo(hoy);
        resultado.add(eSpam);

        return resultado;
    }

}