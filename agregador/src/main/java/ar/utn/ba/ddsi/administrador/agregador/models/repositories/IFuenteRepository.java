package ar.utn.ba.ddsi.administrador.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    /*
    List<Fuente> findAll();
    void save(Fuente fuente);
    Fuente findById(Long id);
    void delete(Fuente fuente);
    */
}