package ar.edu.utn.frba.dds.domain.fuente;

import ar.edu.utn.frba.dds.domain.coleccion.Hecho;
import ar.edu.utn.frba.dds.domain.lectorCSV.LectorDeHechosCSV;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FuenteDataset implements Fuente {
    private LectorDeHechosCSV lectorCSV;
    private String path;
    private List<Hecho> hechos;

    public FuenteDataset(LectorDeHechosCSV lectorCSV, String path) {
        this.lectorCSV = lectorCSV;
        this.path = path;
        this.hechos = new ArrayList<>();
    }

    @Override
    public void importarHechos() {
        this.hechos = lectorCSV.obtenerHechos(this.path, this);
    }

    @Override
    public void agregarHecho(Hecho hecho) {
        this.hechos.add(hecho);
    }

    @Override
    public void eliminarHecho(Hecho hecho) {
        // TODO
    }
}
