package algoritmosDeConsenso;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ConsensoAbsoluto implements AlgoritmoDeConsenso {

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos, List<IHechoRepository> fuentes) {
        return hechos.stream()
            .filter(hecho -> fuentes.stream()
                .allMatch(fuente -> contieneHecho(fuente, hecho))
            )
            .collect(Collectors.toList());
    }

    private boolean contieneHecho(IHechoRepository fuente, Hecho hecho) {
        return fuente.findAll().stream()
            .anyMatch(fuentehecho -> fuentehecho.equals(hecho));
    }
}