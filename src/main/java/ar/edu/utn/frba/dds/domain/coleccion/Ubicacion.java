package ar.edu.utn.frba.dds.domain.coleccion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}