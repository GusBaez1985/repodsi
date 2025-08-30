package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HechoRepository implements IHechoRepository {

	private List<Hecho> hechos;

	public HechoRepository() {
		this.hechos = new ArrayList<>();
	}

	@Override
	public List<Hecho> findAll() {
		return this.hechos;
	}

	@Override
	public void save(Hecho hecho) {
		this.hechos.add(hecho);
	}

	@Override
	public void delete(Hecho hecho) {
		this.hechos.remove(hecho);
	}

    @Override
    public Optional<Hecho> findById(Long id) {
        return this.hechos.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
    }
}