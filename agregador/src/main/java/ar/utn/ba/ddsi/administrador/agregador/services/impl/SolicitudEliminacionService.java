package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
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

    // --- INICIO DE LA CORRECCIÓN ---

    @Override
    public void crearSolicitud(SolicitudEliminacion solicitud) {
        // Correcto: Usamos el estado que ya existe en tu enum.
        solicitud.setEstadoSolicitud(EstadoSolicitud.SIN_REVISAR);

        // Guardamos la solicitud.
        solicitudRepository.save(solicitud);
    }

    // --- FIN DE LA CORRECCIÓN ---

    @Override
    public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo, Contribuyente contribuyente) {
        return new SolicitudEliminacion(motivo, hecho, contribuyente);
    }

    @Override
    public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        Long idDelHechoAEliminar = solicitudEliminacion.getHecho().getId();

        Hecho hechoAEliminar = this.hechoRepository.findById(idDelHechoAEliminar)
                .orElseThrow(() -> new IllegalStateException("El hecho a eliminar no fue encontrado con ID: " + idDelHechoAEliminar));

        if (hechoAEliminar.getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        hechoAEliminar.setEliminado(true);
        this.hechoRepository.save(hechoAEliminar);

        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
        this.solicitudRepository.save(solicitudEliminacion); // Guardar cambio de estado
    }

    @Override
    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
        this.solicitudRepository.save(solicitudEliminacion); // Guardar cambio de estado
    }
}

/*package ar.utn.ba.ddsi.administrador.agregador.services.impl;
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


    @Override
    public SolicitudEliminacion crearSolicitud(SolicitudEliminacion solicitud) {
        // 1. Establecemos el estado inicial de cualquier nueva solicitud como PENDIENTE.
        solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);

        // 2. Usamos el repositorio para guardar la nueva solicitud en la base de datos.
        solicitudRepository.save(solicitud);

        // 3. Devolvemos la solicitud, que ahora ya tiene su estado y está persistida.
        return solicitud;
    }
}
*/