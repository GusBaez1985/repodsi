package ar.edu.utn.frba.dds.models.services.spam;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;

public interface ISpamDetector {
    boolean esSpam(SolicitudEliminacion solicitud);
}