package ar.utn.ba.ddsi.administrador.fuente_dinamica.mappers;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class HechoMapper {

    public static HechoResponseDTO toDto(Hecho hecho) {
        HechoResponseDTO dto = new HechoResponseDTO();
        dto.id = hecho.getId();
        dto.titulo = hecho.getTitulo();
        dto.descripcion = hecho.getDescripcion();
        dto.categoria = hecho.getCategoria();
        dto.latitud = hecho.getUbicacion().getLatitud();
        dto.longitud = hecho.getUbicacion().getLongitud();
        dto.fechaAcontecimiento = hecho.getFecAcontecimiento();
        dto.etiquetas = hecho.getEtiquetas();
        return dto;
    }

    public static List<HechoResponseDTO> toDtoList(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoMapper::toDto)
                .collect(Collectors.toList());
    }
}
