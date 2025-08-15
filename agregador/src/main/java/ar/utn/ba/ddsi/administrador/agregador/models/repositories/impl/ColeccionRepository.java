package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
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

    @Override
    public Coleccion findById(Long id) {
        return null;
    }
}