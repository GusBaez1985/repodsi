package ar.utn.ba.ddsi.agregador.services;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;

public interface ISolicitudEliminacionService {
	public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo);
	public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);
	public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);
}
