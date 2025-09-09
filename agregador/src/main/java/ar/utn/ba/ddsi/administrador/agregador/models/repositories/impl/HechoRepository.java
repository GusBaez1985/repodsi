/*package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class HechoRepository implements IHechoRepository {
    private Long sequence = 0L;
	private List<Hecho> hechos;

	public HechoRepository() {
		this.hechos = new ArrayList<>();
	}

	@Override
	public List<Hecho> findAll() {
		return this.hechos;
	}

    @Override
    public Hecho save(Hecho hecho) {
        if (hecho.getId() == null) {
            // Si el hecho es nuevo (del CSV), le asignamos el siguiente ID de nuestra secuencia.
            hecho.setId(++sequence);
            hechos.add(hecho);
        } else {
            // Si el hecho ya tiene ID (de la fuente dinámica), lo guardamos.
            hechos.removeIf(h -> h.getId().equals(hecho.getId())); // Quitamos el viejo si existía
            hechos.add(hecho);

            // ¡CRÍTICO! Sincronizamos nuestra secuencia para evitar futuras colisiones.
            // Si el ID que vino de afuera es más grande que nuestro contador, actualizamos el contador.
            if (hecho.getId() > this.sequence) {
                this.sequence = hecho.getId();
            }
        }
        return hecho;
    }



	@Override
	public void delete(Hecho hecho) {
		this.hechos.remove(hecho);
	}

    @Override
    public Optional<Hecho> findById(Long id) {
        return this.hechos.stream()
                .filter(h -> Objects.equals(h.getId(), id))
                .findFirst();
    }
}*/