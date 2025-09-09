package ar.utn.ba.ddsi.administrador.agregador.models.repositories;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    /*
	public List<Hecho> findAll();
	//public void save(Hecho hecho);
    Hecho save(Hecho hecho);
	public void delete(Hecho hecho);
    Optional<Hecho> findById(Long id);
    */
}
