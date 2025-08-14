package ar.utn.ba.ddsi.fuente_proxy.models.dtos.input;

import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SolicitudDTO {
    private Long id;
    private String motivo;
    private Long idHecho;
    private Long idContribuyente;
    private Long idAdministrador;
    private LocalDateTime fechaCreacion;

    public static SolicitudDTO toDTO(Long id, String motivo, Long idHecho, Long idContribuyente, Long idAdministrador) {
        return SolicitudDTO
                .builder()
                .id(id)
                .motivo(motivo)
                .idHecho(idHecho)
                .idContribuyente(idContribuyente)
                .idAdministrador(idAdministrador)
                .build();
    }
}
