package ar.utn.ba.ddsi.administrador.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class HechoResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDate fecAcontecimiento;
    private LocalDate fecCarga;


    public HechoResponseDTO(String descripcion) {
        this.descripcion = descripcion;
    }
}