package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    public Coleccion findById(Long id);
    public List<Coleccion> findAll();
    public void save(Coleccion coleccion);
    public void delete(Coleccion coleccion);
}
