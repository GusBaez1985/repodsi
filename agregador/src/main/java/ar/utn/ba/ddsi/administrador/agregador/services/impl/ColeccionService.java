package ar.utn.ba.ddsi.administrador.agregador.services.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.utn.ba.ddsi.administrador.agregador.services.IColeccionService;

public class ColeccionService implements IColeccionService {

	@Override
	public Coleccion crearColeccion(String titulo, String descripcion, CriterioDePertenencia criterio) {
		return new Coleccion(titulo, descripcion, criterio);
	}
}