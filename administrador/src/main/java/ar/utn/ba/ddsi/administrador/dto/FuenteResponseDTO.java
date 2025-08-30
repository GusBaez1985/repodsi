package ar.utn.ba.ddsi.administrador.dto;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import lombok.Getter;

@Getter
public class FuenteResponseDTO {
    private Long id;
    private String tipo;

    // Método estático "from" para convertir una entidad Fuente a este DTO
    public static FuenteResponseDTO from(Fuente fuente) {
        FuenteResponseDTO dto = new FuenteResponseDTO();
        dto.id = fuente.getId();

        // USAMOS: el nombre de la clase
        dto.tipo = fuente.getClass().getSimpleName();

        return dto;
    }
}