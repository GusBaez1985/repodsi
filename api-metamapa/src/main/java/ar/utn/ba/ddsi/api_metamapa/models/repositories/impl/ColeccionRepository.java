package ar.utn.ba.ddsi.api_metamapa.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ColeccionRepository implements IColeccionRepository {
    private final Map<Long, Coleccion> colecciones = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Coleccion findById(Long id) {
        return this.colecciones.get(id);
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(this.colecciones.values());
    }

    @Override
    public void save(Coleccion coleccion) {
        if (coleccion.getId() == null) {
            Long id = idGenerator.getAndIncrement();
            coleccion.setId(id);
        }
        this.colecciones.put(coleccion.getId(), coleccion);
    }

    @Override
    public void delete(Coleccion coleccion) {
        this.colecciones.remove(coleccion.getId());
    }
}
