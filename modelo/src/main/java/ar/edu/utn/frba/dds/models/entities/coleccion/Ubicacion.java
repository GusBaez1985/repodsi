package ar.edu.utn.frba.dds.models.entities.coleccion;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // <-- AÑADIR ESTE IMPORT

@Getter
@Setter
@NoArgsConstructor // <-- AÑADIR ESTA ANOTACIÓN
public class Ubicacion {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}