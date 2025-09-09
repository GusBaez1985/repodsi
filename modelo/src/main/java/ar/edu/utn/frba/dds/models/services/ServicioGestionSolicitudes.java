package ar.edu.utn.frba.dds.models.services;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.models.services.spam.ISpamDetector;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicioGestionSolicitudes {

    private final ISpamDetector spamDetector;
    private final ISolicitudEliminacionRepository solicitudRepository;

    public ServicioGestionSolicitudes(ISpamDetector spamDetector, ISolicitudEliminacionRepository solicitudRepository) {
        if (spamDetector == null || solicitudRepository == null) {
            throw new IllegalArgumentException("spamDetector y solicitudRepository no pueden ser null.");
        }
        this.spamDetector = spamDetector;
        this.solicitudRepository = solicitudRepository;
    }

    public SolicitudEliminacion crearYProcesarSolicitud(String motivo, Hecho hecho, Contribuyente contribuyente) {
        if (contribuyente.getId() == null || hecho == null || motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("id, hecho y motivo no pueden ser nulos o vacíos.");
        }

        SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(motivo, hecho, contribuyente);
        EstadoSolicitud estado = spamDetector.esSpam(nuevaSolicitud)
                ? EstadoSolicitud.RECHAZADA
                : EstadoSolicitud.SIN_REVISAR;
        nuevaSolicitud.setEstadoSolicitud(estado);

        solicitudRepository.save(nuevaSolicitud);
        return nuevaSolicitud;
    }
}