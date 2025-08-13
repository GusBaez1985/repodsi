package ar.edu.utn.frba.dds.domain.coleccion;

import ar.edu.utn.frba.dds.domain.criterios.CriterioDePertenencia;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Coleccion {
    @Setter
    private String titulo;
    @Setter
    private String descripcion;
    private List<Hecho> hechos;
    @Setter
    private CriterioDePertenencia criterio;

    public Coleccion(String titulo, String descripcion, CriterioDePertenencia criterio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterio = criterio;
        this.hechos = new ArrayList<>();
    }
}