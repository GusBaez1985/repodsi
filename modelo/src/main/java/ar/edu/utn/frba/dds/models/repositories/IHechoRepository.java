package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;

import java.util.List;

public interface IHechoRepository {
    public Hecho findById(Long id);
    public List<Hecho> findAll();
    public void save(Hecho hecho);
    public void delete(Hecho hecho);
}
