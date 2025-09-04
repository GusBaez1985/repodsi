package ar.edu.utn.frba.dds.models.entities.fuente;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
public interface Fuente {
    // para identificar cada instancia de fuentes
    Long getId();
    void setId(Long id);

    public void importarHechos();
    public void agregarHecho(Hecho hecho);
    public void eliminarHecho(Hecho hecho);
		public List<Hecho> getHechos();
}