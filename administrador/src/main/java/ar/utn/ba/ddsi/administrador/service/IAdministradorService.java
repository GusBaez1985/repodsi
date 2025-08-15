
package ar.utn.ba.ddsi.administrador.service;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;

import java.util.List;

public interface IAdministradorService {
    List<Coleccion> obtenerTodasLasColecciones();
}
