package ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos;

import java.time.LocalDate;
import java.util.List;

public class HechoResponseDTO {
    public Long id;
    public String titulo;
    public String descripcion;
    public String categoria;
    public String latitud;
    public String longitud;
    public LocalDate fechaAcontecimiento;
    public List<String> etiquetas;

    public HechoResponseDTO() {}
}
