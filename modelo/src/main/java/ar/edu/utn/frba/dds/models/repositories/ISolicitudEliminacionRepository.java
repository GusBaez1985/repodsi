package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudEliminacionRepository {
    public SolicitudEliminacion findById(Long id);
    public List<SolicitudEliminacion> findAll();
    public void save(SolicitudEliminacion solicitudEliminacion);
    public void delete(SolicitudEliminacion solicitudEliminacion);
}
