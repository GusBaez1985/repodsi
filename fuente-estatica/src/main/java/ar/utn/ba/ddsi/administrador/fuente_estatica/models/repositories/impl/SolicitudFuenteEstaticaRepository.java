package ar.utn.ba.ddsi.administrador.fuente_estatica.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.repositories.ISolicitudEliminacionRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SolicitudFuenteEstaticaRepository implements ISolicitudEliminacionRepository {
    private final Map<Long, SolicitudEliminacion> solicitudes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public SolicitudEliminacion findById(Long id) {
        return this.solicitudes.get(id);
    }

    @Override
    public List<SolicitudEliminacion> findAll() {
        return new ArrayList<>(this.solicitudes.values());
    }

    @Override
    public void save(SolicitudEliminacion solicitudEliminacion) {
        if(solicitudEliminacion.getId() == null){
            Long id = idGenerator.getAndIncrement();
            solicitudEliminacion.setId(id);
            this.solicitudes.put(id, solicitudEliminacion);
        } else {
            //Actualización
            this.solicitudes.put(solicitudEliminacion.getId(), solicitudEliminacion);
        }
    }

    @Override
    public void delete(SolicitudEliminacion solicitudEliminacion) {
        this.solicitudes.remove(solicitudEliminacion.getId());
    }
}
