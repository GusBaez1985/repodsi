package ar.utn.ba.ddsi.administrador.agregador.services.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.services.spam.ISpamDetector;
import ar.utn.ba.ddsi.administrador.agregador.services.IAgregadorService;
import java.util.ArrayList;
import java.util.List;

public class AgregadorService implements IAgregadorService {

	private List<Fuente> fuentes;
	private ISpamDetector detectorDeSpam;

	public AgregadorService(List<Fuente> fuentes, ISpamDetector detectorDeSpam) {
		this.fuentes = fuentes;
		this.detectorDeSpam = detectorDeSpam;
	}

	@Override
	public List<Hecho> obtenerTodosLosHechos() {
		List<Hecho> hechosCombinados = new ArrayList<>();
		fuentes.forEach(f -> { f.importarHechos();
			hechosCombinados.addAll(f.getHechos());
		});
		return hechosCombinados;
	}

	@Override
	public List<Hecho> obtenerHechosPorCategoria(String categoria) {
		return obtenerTodosLosHechos()
				.stream()
				.filter(h -> h.getCategoria().equalsIgnoreCase(categoria))
				.toList();
	}

    @Override
    public void procesarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        /*
        if (detectorDeSpam.esSpam(solicitud)) {
            solicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
            solicitud.getHecho().setEliminado(false);
        } else {
            solicitud.setEstadoSolicitud(EstadoSolicitud.APROBADA);
            solicitud.getHecho().setEliminado(true);
        }*/
    }
}