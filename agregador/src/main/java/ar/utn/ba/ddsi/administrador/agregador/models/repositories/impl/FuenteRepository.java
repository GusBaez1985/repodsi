/*
package ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IFuenteRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong; // <--- Importante

@Repository
public class FuenteRepository implements IFuenteRepository {

    private final List<Fuente> fuentes = new ArrayList<>();
    // Usamos un contador para simular el auto-incremento, igual que en ColeccionRepository
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public List<Fuente> findAll() {
        return this.fuentes;
    }

    @Override
    public void save(Fuente fuente) {
        if (fuente.getId() == null) {
            // Si es una fuente nueva, le asignamos un ID incremental
            //fuente.setId(sequence.incrementAndGet());
            this.fuentes.add(fuente);
        } else {
            // Si es una modificación, la reemplazamos
            this.fuentes.removeIf(f -> Objects.equals(f.getId(), fuente.getId()));
            this.fuentes.add(fuente);
        }
    }

    @Override
    public Fuente findById(Long id) {
        return this.fuentes.stream()
                .filter(f -> Objects.equals(f.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(Fuente fuente) {
        if (fuente != null) {
            this.fuentes.removeIf(f -> Objects.equals(f.getId(), fuente.getId()));
        }
    }
}*/