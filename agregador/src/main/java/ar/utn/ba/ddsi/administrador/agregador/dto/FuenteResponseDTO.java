package ar.utn.ba.ddsi.administrador.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FuenteResponseDTO {
    private Long id;
    private String tipo;
    private String nombre;
}