package ar.edu.utn.frba.dds.models.entities.interfaces;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;

import java.util.List;

public interface AlgoritmoDeConsenso {
    List<Hecho> aplicar(List<Hecho> hechos, List<IHechoRepository> fuentes);
}