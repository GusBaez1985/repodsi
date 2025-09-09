package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("contribuyente")
public class FuenteContribuyente extends Fuente {
    @Column(name = "registrado")
    private boolean esRegistrado; // flag de edicion! si escala se debe modificar!!
    public FuenteContribuyente(boolean esRegistrado) {
        this.esRegistrado = esRegistrado;
        super.hechos = new ArrayList<>();
    }

    public FuenteContribuyente() {
        this.esRegistrado = false;
        super.hechos = new ArrayList<>();
    }

    @Override
    public void importarHechos() {
        // TODO
    }
}