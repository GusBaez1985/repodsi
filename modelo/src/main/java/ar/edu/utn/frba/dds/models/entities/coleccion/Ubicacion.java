package ar.edu.utn.frba.dds.models.entities.coleccion;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // <-- AÑADIR ESTE IMPORT

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Ubicacion {
    private String latitud;
    private String longitud;

    public Ubicacion(String latitud, String longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}