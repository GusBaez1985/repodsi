package ar.utn.ba.ddsi.administrador.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos que no necesitemos
public class SolicitudEliminacionDTO {
    private long id;
    private String motivo;
    private long idHecho;
    private String estadoSolicitud; // Recibimos el estado como un simple String
}