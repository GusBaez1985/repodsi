package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import java.util.Collection;
import java.util.List;

public interface Fuente {
    public void importarHechos();
    public void agregarHecho(Hecho hecho);
    public void eliminarHecho(Hecho hecho);
		public List<Hecho> getHechos();
}