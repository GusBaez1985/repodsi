package ar.edu.utn.frba.dds.domain.criterios;

import ar.edu.utn.frba.dds.domain.coleccion.Hecho;

public interface CriterioDePertenencia {
    public Boolean cumpleCriterio(Hecho hecho);
}