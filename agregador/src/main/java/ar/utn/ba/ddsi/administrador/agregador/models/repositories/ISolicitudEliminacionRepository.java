package ar.utn.ba.ddsi.administrador.agregador.models.repositories;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import java.util.List;

public interface ISolicitudEliminacionRepository {
	public List<SolicitudEliminacion> findAll();
	public SolicitudEliminacion save(SolicitudEliminacion solicitud);
	public void delete(SolicitudEliminacion solicitud);
}

