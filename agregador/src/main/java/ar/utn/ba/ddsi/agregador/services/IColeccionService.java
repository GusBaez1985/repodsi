package ar.utn.ba.ddsi.agregador.services;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;

public interface IColeccionService {
	public Coleccion crearColeccion(String titulo, String descripcion, CriterioDePertenencia criterio);
}