package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Hecho {
    @Setter
    private Long id;
    @Setter
    private String titulo;
    @Setter
    private String descripcion;
    @Setter
    private TipoHecho tipoHecho;
    @Setter
    private String categoria;
    @Setter
    private Ubicacion ubicacion;
    @Setter
    private LocalDate fecAcontecimiento;
    private LocalDate fecCarga;
    private Fuente fuente;
    @Setter
    private Boolean eliminado;
    private List<String> etiquetas;
    @Setter
    private EstadoRevision estadoRevision;

    public static Hecho of(String titulo, String descripcion, TipoHecho tipoHecho, String categoria, Ubicacion ubicacion, LocalDate fecAcontecimiento, Fuente fuente) {
        return Hecho
                .builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .tipoHecho(tipoHecho)
                .categoria(categoria)
                .ubicacion(ubicacion)
                .fecAcontecimiento(fecAcontecimiento)
                .fecCarga(LocalDate.now())
                .fuente(fuente)
                .eliminado(false)
                .etiquetas(new ArrayList<>())
                .build();
    }

    public void agregarEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }
}
