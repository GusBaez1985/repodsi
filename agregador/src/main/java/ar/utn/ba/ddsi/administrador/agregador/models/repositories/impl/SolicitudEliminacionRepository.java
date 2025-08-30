package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.ISolicitudEliminacionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
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
	public SolicitudEliminacion save(SolicitudEliminacion solicitud) {
		this.solicitudes.add(solicitud);
        return solicitud;
    }

	@Override
	public void delete(SolicitudEliminacion solicitud) {
		this.solicitudes.remove(solicitud);
	}
}
