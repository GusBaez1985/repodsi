package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class FuenteCargaManual implements Fuente {
    private Long id;

    public FuenteCargaManual(String s) {
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