package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    Coleccion findById(Long id);
    List<Coleccion> findAll();
    void save(Coleccion coleccion);
    void delete(Coleccion coleccion);


}
