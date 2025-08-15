package ar.utn.ba.ddsi.administrador.agregador.services.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.utn.ba.ddsi.administrador.agregador.services.ISolicitudEliminacionService;

public class SolicitudEliminacionService implements ISolicitudEliminacionService {

	@Override
	public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo) {
		SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion(motivo, hecho.getId(), hecho.getId());
		return nuevaSolicitudEliminacion;
	}

	@Override
	public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
		/*
        if (solicitudEliminacion.getHecho().getEliminado()) {
		throw new IllegalStateException("El hecho ya se encuentra eliminado.");
		}
	solicitudEliminacion.getHecho().setEliminado(true);*/
	solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
	}

	@Override
	public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        /*
		if (solicitudEliminacion.getHecho().getEliminado()) {
		throw new IllegalStateException("El hecho ya se encuentra eliminado.");
		}
	solicitudEliminacion.getHecho().setEliminado(false);*/
	solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
	}
}