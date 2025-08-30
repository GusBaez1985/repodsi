package ar.utn.ba.ddsi.administrador.agregador.services.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.ISolicitudEliminacionService;
import org.springframework.stereotype.Service;


@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    private final IHechoRepository hechoRepository;
    private final ISolicitudEliminacionRepository solicitudRepository;

    public SolicitudEliminacionService(IHechoRepository hechoRepository, ISolicitudEliminacionRepository solicitudRepository) {
        this.hechoRepository = hechoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo) {
        SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(motivo, hecho.getId(), hecho.getId());

        return nuevaSolicitud;
    }

    @Override
    public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        Long idDelHechoAEliminar = solicitudEliminacion.getIdHecho();

        // Usamos el método que acabamos de crear en el repositorio
        Hecho hechoAEliminar = this.hechoRepository.findById(idDelHechoAEliminar)
                .orElseThrow(() -> new IllegalStateException("El hecho a eliminar no fue encontrado con ID: " + idDelHechoAEliminar));

        if (hechoAEliminar.getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        hechoAEliminar.setEliminado(true);
        this.hechoRepository.save(hechoAEliminar); // Guardamos el cambio en el hecho

        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
    }

    @Override
    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
    }
}
/* Comento porque como estaba espera un hecho, y en eliminar espera un id y se encarga el repo
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

	@Override
	public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo) {
		SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion(motivo, hecho.getId(), hecho.getId());
		return nuevaSolicitudEliminacion;
	}

	@Override
	public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        if (solicitudEliminacion.getHecho().getEliminado()) {
		throw new IllegalStateException("El hecho ya se encuentra eliminado.");
		}
	solicitudEliminacion.getHecho().setEliminado(true);

	solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
	}

	@Override
	public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {

		if (solicitudEliminacion.getHecho().getEliminado()) {
		throw new IllegalStateException("El hecho ya se encuentra eliminado.");
		}
	solicitudEliminacion.getHecho().setEliminado(false);
	solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
	}
}
*/
