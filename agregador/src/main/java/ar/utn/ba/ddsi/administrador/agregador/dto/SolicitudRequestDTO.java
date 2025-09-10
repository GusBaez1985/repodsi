package ar.utn.ba.ddsi.administrador.agregador.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudRequestDTO {
    private String motivo;
    private Long hechoId;
    private Long contribuyenteId;
}