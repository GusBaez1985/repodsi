package ar.utn.ba.ddsi.administrador.agregador.models.repositories;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import java.util.List;
import java.util.Optional;

public interface IHechoRepository {
	public List<Hecho> findAll();
	public void save(Hecho hecho);
	public void delete(Hecho hecho);
    Optional<Hecho> findById(Long id);
}
