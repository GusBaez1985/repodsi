package ar.utn.ba.ddsi.administrador.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EstadisticasSpamDTO {
    private long totalSolicitudes;
    private long solicitudesRechazadasPorSpam;
    private long solicitudesSinRevisar;
    private long solicitudesAprobadas;
}