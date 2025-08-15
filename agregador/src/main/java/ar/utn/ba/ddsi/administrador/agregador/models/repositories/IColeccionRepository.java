package ar.utn.ba.ddsi.administrador.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import java.util.List;

public interface IColeccionRepository {
	List<Coleccion> findAll();
	void save(Coleccion coleccion);
	void delete(Coleccion coleccion);

    Coleccion findById(Long id);
}
