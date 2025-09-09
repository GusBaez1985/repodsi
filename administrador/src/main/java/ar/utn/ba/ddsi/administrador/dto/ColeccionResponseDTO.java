// Archivo: ColeccionResponseDTO.java

package ar.utn.ba.ddsi.administrador.dto;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import lombok.Getter;
import java.util.List; // <--- Importar
import java.util.stream.Collectors; // <--- Importar

@Getter
public class ColeccionResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipoAlgoritmo;
    private List<FuenteResponseDTO> fuentes; // <-- AÑADIMOS LA LISTA DE FUENTES

    public static ColeccionResponseDTO from(Coleccion coleccion) {
        ColeccionResponseDTO dto = new ColeccionResponseDTO();
        dto.id = coleccion.getId();
        dto.titulo = coleccion.getTitulo();
        dto.descripcion = coleccion.getDescripcion();
        dto.tipoAlgoritmo = coleccion.getTipoAlgoritmo();
        if (coleccion.getFuentes() != null) {
            dto.fuentes = coleccion.getFuentes().stream()
                    .map(FuenteResponseDTO::from)
                    .collect(Collectors.toList());
        }
        return dto;
    }
}