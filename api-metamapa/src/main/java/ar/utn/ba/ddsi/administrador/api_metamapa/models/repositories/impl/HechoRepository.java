package ar.utn.ba.ddsi.administrador.api_metamapa.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechoRepository implements IHechoRepository {
    private final Map<Long, Hecho> hechos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Hecho findById(Long id) {
        return this.hechos.get(id);
    }

    @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public void save(Hecho hecho) {
        if(hecho.getId() == null){
            Long id = idGenerator.getAndIncrement();
            hecho.setId(id);
            this.hechos.put(id, hecho);
        } else {
            //Actualización
            this.hechos.put(hecho.getId(), hecho);
        }
    }

    @Override
    public void delete(Hecho hecho) {
        this.hechos.remove(hecho.getId());
    }
}
