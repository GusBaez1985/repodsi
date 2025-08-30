package ar.edu.utn.frba.dds.models.entities.adapter;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;

import java.util.List;

public interface IAdapterAPI {
    public List<Hecho> obtenerHechos();
}
