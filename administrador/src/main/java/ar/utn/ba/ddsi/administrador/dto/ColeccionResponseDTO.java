
package ar.utn.ba.ddsi.administrador.dto;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import lombok.Getter;

@Getter
public class ColeccionResponseDTO {
    private String titulo;
    private String descripcion;
    private String tipoAlgoritmo;

    public static ColeccionResponseDTO from(Coleccion coleccion) {
        ColeccionResponseDTO dto = new ColeccionResponseDTO();
        dto.titulo = coleccion.getTitulo();
        dto.descripcion = coleccion.getDescripcion();
        dto.tipoAlgoritmo = coleccion.getAlgoritmoDeConsenso() == null
                ? "ninguno"
                : coleccion.getAlgoritmoDeConsenso().getClass().getSimpleName();
        return dto;
    }
}