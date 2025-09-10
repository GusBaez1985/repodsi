package ar.utn.ba.ddsi.administrador.agregador.services;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.utn.ba.ddsi.administrador.agregador.dto.EstadisticasSpamDTO;
import ar.utn.ba.ddsi.administrador.agregador.dto.SolicitudRequestDTO; // <-- IMPORTANTE
import java.util.List;

public interface ISolicitudEliminacionService {

    // Cambiamos la firma para que acepte el DTO
    void crearSolicitud(SolicitudRequestDTO dto);

    // Mantenemos los métodos que sí se usan
    void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);

    void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion);

    // Nuevo método para estadísticas
    List<SolicitudEliminacion> obtenerSolicitudesSpam();

    public EstadisticasSpamDTO obtenerEstadisticasSpam();
}