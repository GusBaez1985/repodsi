package ar.utn.ba.ddsi.administrador.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import java.util.List;

public interface IFuenteRepository {
    List<Fuente> findAll();
    void save(Fuente fuente);
    Fuente findById(Long id);
    void delete(Fuente fuente);
}