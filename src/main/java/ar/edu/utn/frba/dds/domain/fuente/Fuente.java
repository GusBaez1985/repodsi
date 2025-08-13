package ar.edu.utn.frba.dds.domain.fuente;

import ar.edu.utn.frba.dds.domain.coleccion.Hecho;

public interface Fuente {
    public void importarHechos();
    public void agregarHecho(Hecho hecho);
    public void eliminarHecho(Hecho hecho);
}