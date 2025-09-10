package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
import ar.edu.utn.frba.dds.models.services.spam.SpamDetector;
import ar.utn.ba.ddsi.administrador.agregador.dto.SolicitudRequestDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IContribuyenteRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.ISolicitudEliminacionService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    private final IHechoRepository hechoRepository;
    private final ISolicitudEliminacionRepository solicitudRepository;
    private final IContribuyenteRepository contribuyenteRepository;
    private final SpamDetector spamDetector;

    public SolicitudEliminacionService(IHechoRepository hechoRepository, ISolicitudEliminacionRepository solicitudRepository, IContribuyenteRepository contribuyenteRepository) {
        this.hechoRepository = hechoRepository;
        this.solicitudRepository = solicitudRepository;
        this.contribuyenteRepository = contribuyenteRepository;
        this.spamDetector = new SpamDetector();
    }

    @Override
    public void crearSolicitud(SolicitudRequestDTO dto) {
        Hecho hechoReal = hechoRepository.findById(dto.getHechoId())
                .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + dto.getHechoId()));

        Contribuyente contribuyenteReal = contribuyenteRepository.findById(dto.getContribuyenteId())
                .orElseThrow(() -> new RuntimeException("Contribuyente no encontrado con id: " + dto.getContribuyenteId()));

        SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(dto.getMotivo(), hechoReal, contribuyenteReal);

        if (spamDetector.esSpam(nuevaSolicitud)) {
            nuevaSolicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
            System.out.println("Solicitud rechazada automáticamente por SPAM.");
        } else {
            nuevaSolicitud.setEstadoSolicitud(EstadoSolicitud.SIN_REVISAR);
        }
        solicitudRepository.save(nuevaSolicitud);
    }

    @Override
    public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        // La lógica para aprobar no cambia
        Long idDelHechoAEliminar = solicitudEliminacion.getHecho().getId();
        Hecho hechoAEliminar = this.hechoRepository.findById(idDelHechoAEliminar)
                .orElseThrow(() -> new IllegalStateException("El hecho a eliminar no fue encontrado con ID: " + idDelHechoAEliminar));

        if (hechoAEliminar.getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        hechoAEliminar.setEliminado(true);
        this.hechoRepository.save(hechoAEliminar);
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
        this.solicitudRepository.save(solicitudEliminacion);
    }

    @Override
    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {
        // La lógica para rechazar no cambia
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
        this.solicitudRepository.save(solicitudEliminacion);
    }

    @Override
    public List<SolicitudEliminacion> obtenerSolicitudesSpam() {
        return solicitudRepository.findAll().stream()
                .filter(spamDetector::esSpam)
                .collect(Collectors.toList());
    }
}