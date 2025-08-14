package ar.utn.ba.ddsi.agregador.models.repositories.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.utn.ba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import java.util.ArrayList;
import java.util.List;

public class SolicitudEliminacionRepository implements ISolicitudEliminacionRepository {

	private List<SolicitudEliminacion> solicitudes;

	public SolicitudEliminacionRepository() {
		this.solicitudes = new ArrayList<>();
	}

	@Override
	public List<SolicitudEliminacion> findAll() {
		return this.solicitudes;
	}

	@Override
	public void save(SolicitudEliminacion solicitud) {
		this.solicitudes.add(solicitud);
	}

	@Override
	public void delete(SolicitudEliminacion solicitud) {
		this.solicitudes.remove(solicitud);
	}
}
