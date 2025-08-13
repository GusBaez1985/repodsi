package ar.edu.utn.frba.dds.domain.criterios;

import ar.edu.utn.frba.dds.domain.coleccion.Hecho;

import java.time.LocalDate;

public class CriterioFechaEntre implements CriterioDePertenencia{
    private LocalDate desde;
    private LocalDate hasta;

    public CriterioFechaEntre(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    public Boolean cumpleCriterio(Hecho hecho) {
        return !hecho.getFecAcontecimiento().isBefore(desde) &&
                !hecho.getFecAcontecimiento().isAfter(hasta);
    }
}