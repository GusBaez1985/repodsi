package ar.edu.utn.frba.dds.domain.criterios;

import ar.edu.utn.frba.dds.domain.coleccion.Hecho;

public class CriterioPorCategoria implements CriterioDePertenencia {

    private String categoria;

    public CriterioPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        return hecho.getCategoria().equalsIgnoreCase(categoria);
    }
}