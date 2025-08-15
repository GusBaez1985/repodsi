package ar.utn.ba.ddsi.administrador.service;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;

public class CriterioDummy implements CriterioDePertenencia {
    @Override
    public Boolean cumpleCriterio(Hecho hecho) {
        return true; // acepta todo, sirve para testear
    }
}