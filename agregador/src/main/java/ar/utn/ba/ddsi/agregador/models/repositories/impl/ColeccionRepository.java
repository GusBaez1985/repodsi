package ar.utn.ba.ddsi.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.agregador.models.repositories.IColeccionRepository;
import java.util.ArrayList;
import java.util.List;

public class ColeccionRepository implements IColeccionRepository {

	private List<Coleccion> colecciones;

	public ColeccionRepository() {
		this.colecciones = new ArrayList<>();
	}

	@Override
	public List<Coleccion> findAll() {
		return this.colecciones;
	}

	@Override
	public void save(Coleccion coleccion) {
		this.colecciones.add(coleccion);
	}

	@Override
	public void delete(Coleccion coleccion) {
		this.colecciones.remove(coleccion);
	}
}