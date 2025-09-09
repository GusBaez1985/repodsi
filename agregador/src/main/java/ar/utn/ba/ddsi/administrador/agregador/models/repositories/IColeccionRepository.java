package ar.utn.ba.ddsi.administrador.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//public interface IColeccionRepository {
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
    /*
	List<Coleccion> findAll();
	void save(Coleccion coleccion);
	void delete(Coleccion coleccion);

    Coleccion findById(Long id);
    */
    }

