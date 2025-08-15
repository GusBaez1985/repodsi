package ar.utn.ba.ddsi.administrador.agregador.services.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.utn.ba.ddsi.administrador.agregador.services.IHechoService;
import java.util.List;

public class HechoService implements IHechoService {
	@Override
	public List<Hecho> obtenerHechos(Coleccion coleccion, List<CriterioDePertenencia> filtros) {
		List<Hecho> hechos = coleccion.getHechos();

		if (filtros.isEmpty()) {
			return hechos;
		}
		return hechos.stream()
				.filter(hecho -> filtros.stream().allMatch(filtro -> filtro.cumpleCriterio(hecho))).toList();
	}
}