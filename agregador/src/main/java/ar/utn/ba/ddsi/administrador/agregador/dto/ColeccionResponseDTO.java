package ar.utn.ba.ddsi.administrador.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ColeccionResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipoAlgoritmo;
    private List<FuenteResponseDTO> fuentes;
}