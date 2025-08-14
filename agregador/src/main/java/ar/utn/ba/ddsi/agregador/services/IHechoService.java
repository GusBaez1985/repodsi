package ar.utn.ba.ddsi.agregador.services;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import java.util.List;

public interface IHechoService {
	public List<Hecho> obtenerHechos(Coleccion coleccion, List<CriterioDePertenencia> filtros);
}