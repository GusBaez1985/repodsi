package ar.utn.ba.ddsi.administrador.dto;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import lombok.Getter;

@Getter
public class HechoResponseDTO {
    private final String descripcion;

    public HechoResponseDTO(String descripcion) {
        this.descripcion = descripcion;
    }

    public static HechoResponseDTO from(Hecho hecho) {
        return new HechoResponseDTO(hecho.getDescripcion());
    }
}

