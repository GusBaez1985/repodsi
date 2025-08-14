package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FuenteContribuyente implements Fuente {
    private boolean esRegistrado; // flag de edicion! si escala se debe modificar!!

    public FuenteContribuyente(boolean esRegistrado) {
        this.esRegistrado = esRegistrado;
    }

    public FuenteContribuyente() {
        this.esRegistrado = false;
    }

    @Override
    public void importarHechos() {
        // TODO
    }

    @Override
    public void agregarHecho(Hecho hecho) {

    }

    @Override
    public void eliminarHecho(Hecho hecho) {

    }

    @Override
    public List<Hecho> getHechos() {
        return List.of();
    }
}