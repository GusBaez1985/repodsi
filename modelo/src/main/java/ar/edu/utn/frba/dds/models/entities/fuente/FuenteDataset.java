package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.lectorCSV.LectorDeHechosCSV;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("dataset")
public class FuenteDataset extends Fuente {
    @Transient
    private LectorDeHechosCSV lectorCSV;
    @Column(name = "path")
    private String path;

    public FuenteDataset(LectorDeHechosCSV lectorCSV, String path) {
        this.lectorCSV = lectorCSV;
        this.path = path;
        super.hechos = new ArrayList<>();
    }

    @Override
    public void importarHechos() {
        super.hechos = lectorCSV.obtenerHechos(this.path, this);
    }
}