package ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos;

import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HechoEdicionDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDate fechaAcontecimiento;
}