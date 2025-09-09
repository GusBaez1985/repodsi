/*
package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ColeccionRepository implements IColeccionRepository {

    private final List<Coleccion> colecciones = new ArrayList<>();
    // Usamos un contador atómico para simular el auto-incremento de una base de datos.
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public List<Coleccion> findAll() {
        return this.colecciones;
    }

    @Override
    public void save(Coleccion coleccion) {
        if (coleccion.getId() == null) {
            // Si es una colección nueva (sin ID), le asignamos uno y la agregamos.
            coleccion.setId(sequence.incrementAndGet());
            this.colecciones.add(coleccion);
        } else {
            // Si ya tiene un ID, es una modificación. La buscamos y la reemplazamos.
            // Primero borramos la versión vieja para evitar duplicados.
            this.colecciones.removeIf(c -> Objects.equals(c.getId(), coleccion.getId()));
            this.colecciones.add(coleccion); // Añadimos la versión nueva/actualizada.
        }
    }

    @Override
    public void delete(Coleccion coleccion) {
        if (coleccion != null) {
            this.colecciones.removeIf(c -> Objects.equals(c.getId(), coleccion.getId()));
        }
    }

    @Override
    public Coleccion findById(Long id) {
        // Implementamos la lógica de búsqueda real.
        return this.colecciones.stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst()
                .orElse(null); // Devuelve null si no la encuentra, como se esperaba.
    }
}
/*
public class ColeccionRepository implements IColeccionRepository {

	private List<Coleccion> colecciones;

	public ColeccionRepository() {
		this.colecciones = new ArrayList<>();
	}

	@Override
	public List<Coleccion> findAll() {
		return this.colecciones;
	}

	@Override
	public void save(Coleccion coleccion) {
		this.colecciones.add(coleccion);
	}

	@Override
	public void delete(Coleccion coleccion) {
		this.colecciones.remove(coleccion);
	}

    @Override
    public Coleccion findById(Long id) {
        return null;
    }
}*/