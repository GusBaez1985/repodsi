package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "hecho")
public class Hecho {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Setter
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Setter
    @Enumerated(EnumType.STRING)
    private TipoHecho tipoHecho;

    @Setter
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Setter
    @Embedded
    private Ubicacion ubicacion;

    @Setter
    @Column(name = "fecha_acontecimiento", nullable = false)
    private LocalDateTime fecAcontecimiento;

    @Column(name = "fecha_carga", nullable = false)
    private LocalDateTime fecCarga;

    @Setter
    @ManyToOne
    @JoinColumn(name = "fuente_id")
    @JsonIgnore
    private Fuente fuente;

    @Setter
    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado;

    @ElementCollection
    @CollectionTable(name = "hecho_etiqueta", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
    @Column(name = "etiqueta")
    private List<String> etiquetas;

    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoRevision estadoRevision;

    public static Hecho of(String titulo, String descripcion, TipoHecho tipoHecho, String categoria, Ubicacion ubicacion, LocalDateTime fecAcontecimiento, Fuente fuente) {
        return Hecho
                .builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .tipoHecho(tipoHecho)
                .categoria(categoria)
                .ubicacion(ubicacion)
                .fecAcontecimiento(fecAcontecimiento)
                .fecCarga(LocalDateTime.now())
                .fuente(fuente)
                .eliminado(false)
                .etiquetas(new ArrayList<>())
                .build();
    }

    public void agregarEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }
}
