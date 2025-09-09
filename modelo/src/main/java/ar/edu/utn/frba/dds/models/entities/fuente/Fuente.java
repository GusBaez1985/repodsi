package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoDeFuente" // Este será el campo extra en el JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FuenteContribuyente.class, name = "contribuyente"),
        @JsonSubTypes.Type(value = FuenteCargaManual.class, name = "manual"),
        @JsonSubTypes.Type(value = FuenteDataset.class, name = "dataset")
})

@Getter
@Entity
@Table(name = "fuente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Fuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @OneToMany(mappedBy = "fuente", cascade = CascadeType.ALL)
    protected List<Hecho> hechos;

    public void importarHechos() {}

    public void agregarHecho(Hecho hecho) {
        this.hechos.add(hecho);
    }

    public void eliminarHecho(Hecho hecho) {
        this.hechos.remove(hecho);
    }

    public List<Hecho> obtenerHechos() {
        return this.hechos;
    }
}
