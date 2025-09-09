package ar.utn.ba.ddsi.administrador.agregador.services;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;

public interface ISolicitudEliminacionService {
	public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo, Contribuyente contribuyente);
	public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);
	public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);

    void crearSolicitud(SolicitudEliminacion solicitud);
}
