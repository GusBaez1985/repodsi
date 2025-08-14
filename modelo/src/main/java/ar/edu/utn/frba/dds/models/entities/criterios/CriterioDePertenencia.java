package ar.edu.utn.frba.dds.models.entities.criterios;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;

public interface CriterioDePertenencia {
    public Boolean cumpleCriterio(Hecho hecho);
}