package ar.utn.ba.ddsi.administrador.dto;

import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
@lombok.Getter

public class ColeccionDTO {
    public String titulo;
    public String descripcion;
    private String tipoAlgoritmo;  // nuevo campo opcional
}
