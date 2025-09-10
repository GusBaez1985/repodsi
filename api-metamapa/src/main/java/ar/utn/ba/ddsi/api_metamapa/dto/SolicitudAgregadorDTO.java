package ar.utn.ba.ddsi.api_metamapa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudAgregadorDTO {
    private String motivo;
    private Long hechoId;
    private Long contribuyenteId;

    public SolicitudAgregadorDTO() {
    }

    // Creamos manualmente el constructor que el servicio necesita.
    public SolicitudAgregadorDTO(String motivo, Long hechoId, Long contribuyenteId) {
        this.motivo = motivo;
        this.hechoId = hechoId;
        this.contribuyenteId = contribuyenteId;
    }
}