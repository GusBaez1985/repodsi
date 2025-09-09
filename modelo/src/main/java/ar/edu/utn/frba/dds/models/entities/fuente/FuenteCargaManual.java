package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@DiscriminatorValue("carga_manual")
public class FuenteCargaManual extends Fuente {

    private String nombre;
    public FuenteCargaManual(String s) {
        this.nombre = s;
        super.hechos = new ArrayList<>();
    }

    public FuenteCargaManual() {
        super.hechos = new ArrayList<>();
    }

    @Override
    public void importarHechos() {
        // TODO
    }
}