
package ar.utn.ba.ddsi.administrador.service;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.administrador.dto.SolicitudEliminacionDTO;

import java.util.List;

public interface IAdministradorService {
    List<Coleccion> obtenerTodasLasColecciones();
    List<SolicitudEliminacionDTO> obtenerSolicitudesDeEliminacion();
}
