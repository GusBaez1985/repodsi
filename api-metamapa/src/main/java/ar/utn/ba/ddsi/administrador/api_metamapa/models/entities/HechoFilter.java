package ar.utn.ba.ddsi.administrador.api_metamapa.models.entities;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Stream;

public class HechoFilter {
    private final Map<String, String> filtros;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public HechoFilter(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public Stream<Hecho> aplicar(Stream<Hecho> hechos) {
        Stream<Hecho> stream = hechos;

        if (filtros.containsKey("categoria")) {
            String cat = filtros.get("categoria").toLowerCase();
            stream = stream.filter(h -> h.getCategoria() != null && h.getCategoria().toLowerCase().contains(cat));
        }

        if (filtros.containsKey("fecha_reporte_desde")) {
            LocalDate desde = LocalDate.parse(filtros.get("fecha_reporte_desde"), formatter);
            stream = stream.filter(h -> h.getFecCarga() != null && !h.getFecCarga().isBefore(desde));
        }

        if (filtros.containsKey("fecha_reporte_hasta")) {
            LocalDate hasta = LocalDate.parse(filtros.get("fecha_reporte_hasta"), formatter);
            stream = stream.filter(h -> h.getFecCarga() != null && !h.getFecCarga().isAfter(hasta));
        }

        if (filtros.containsKey("fecha_acontecimiento_desde")) {
            LocalDate desde = LocalDate.parse(filtros.get("fecha_acontecimiento_desde"), formatter);
            stream = stream.filter(h -> h.getFecAcontecimiento() != null && !h.getFecAcontecimiento().isBefore(desde));
        }

        if (filtros.containsKey("fecha_acontecimiento_hasta")) {
            LocalDate hasta = LocalDate.parse(filtros.get("fecha_acontecimiento_hasta"), formatter);
            stream = stream.filter(h -> h.getFecAcontecimiento() != null && !h.getFecAcontecimiento().isAfter(hasta));
        }

        if (filtros.containsKey("ubicacion")) {
            String[] partes = filtros.get("ubicacion").split(",");
            if (partes.length == 2) {
                String latFiltro = partes[0].trim();
                String lonFiltro = partes[1].trim();

                stream = stream.filter(h -> {
                    if (h.getUbicacion() == null) return false;
                    return latFiltro.equals(h.getUbicacion().getLatitud())
                            && lonFiltro.equals(h.getUbicacion().getLongitud());
                });
            }
        }

        return stream;
    }
}
