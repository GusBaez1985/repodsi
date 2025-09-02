package ar.utn.ba.ddsi.administrador.agregador.dto;

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
    private String latitud;
    private String longitud;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas;
}