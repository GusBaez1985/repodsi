package ar.utn.ba.ddsi.agregador.models.repositories;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import java.util.List;

public interface IHechoRepository {
	public List<Hecho> findAll();
	public void save(Hecho hecho);
	public void delete(Hecho hecho);
}
