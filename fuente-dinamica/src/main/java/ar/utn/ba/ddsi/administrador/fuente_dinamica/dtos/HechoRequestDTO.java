package ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos;

import java.time.LocalDate;
// luego se podrías validar los campos o mapearlos mejor, pero esto sirve como primer paso funcional.

public class HechoRequestDTO {
    public String titulo;
    public String descripcion;
    public String categoria;
    public String latitud;
    public String longitud;
    public String fechaAcontecimiento;
}
