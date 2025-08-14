package ar.utn.ba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import java.util.List;

public interface IColeccionRepository {
	public List<Coleccion> findAll();
	public void save(Coleccion coleccion);
	public void delete(Coleccion coleccion);
}
