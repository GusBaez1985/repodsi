package ar.utn.ba.ddsi.api_metamapa.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class HechoFuenteDinamicaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;   // <-- Molde exacto del JSON
    private String longitud;  // <-- Molde exacto del JSON
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas;
}