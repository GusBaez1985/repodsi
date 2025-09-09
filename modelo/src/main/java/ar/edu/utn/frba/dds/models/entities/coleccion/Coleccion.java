package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Transient
    private CriterioDePertenencia criterio;

    // --- SOLUCIÓN INCONVENIENTE 2: FetchType.EAGER ---
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "coleccion_hecho",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechos;

    @JsonIgnore
    @Transient
    private AlgoritmoDeConsenso algoritmoDeConsenso;

    // --- SOLUCIÓN INCONVENIENTE 1: Persistir el tipo de Algoritmo ---
    @Column(name = "tipo_algoritmo")
    private String tipoAlgoritmo;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "coleccion_hecho_consensuado",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechosConsensuados;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "coleccion_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id")
    )
    private List<Fuente> fuentes;

    // ... Constructores y otros métodos no cambian ...
    public Coleccion() {
        this.hechos = new ArrayList<>();
        this.hechosConsensuados = new ArrayList<>();
        this.fuentes = new ArrayList<>();
    }

    public Coleccion(String titulo, String descripcion, CriterioDePertenencia criterio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterio = criterio;
        this.hechos = new ArrayList<>();
        this.algoritmoDeConsenso = null;
        this.hechosConsensuados = new ArrayList<>();
        this.fuentes = new ArrayList<>();
    }

    public void agregarHecho(Hecho hecho) {
        hechos.add(hecho);
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }
}
/*package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT") // Le decimos a JPA que use un tipo de columna para texto largo
    private String descripcion;


    @Transient
    private CriterioDePertenencia criterio;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "coleccion_hecho",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechos;

    @JsonIgnore
    @Transient
    private AlgoritmoDeConsenso algoritmoDeConsenso; // puede ser null

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "coleccion_hecho_consensuado",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechosConsensuados;

    // REVISAR @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "coleccion_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id")
    )
    private List<Fuente> fuentes;

    public Coleccion() {
        this.hechos = new ArrayList<>();
        this.hechosConsensuados = new ArrayList<>();
        this.fuentes = new ArrayList<>();
    }

    public Coleccion(String titulo, String descripcion, CriterioDePertenencia criterio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterio = criterio;
        this.hechos = new ArrayList<>();
        this.algoritmoDeConsenso = null;
        this.hechosConsensuados = new ArrayList<>();
        this.fuentes = new ArrayList<>();
    }

    public void agregarHecho(Hecho hecho) {
        hechos.add(hecho);
    }

    public void ejecutarAlgoritmoDeConsenso(List<IHechoRepository> fuentes) {
        if (algoritmoDeConsenso == null) {
            this.hechosConsensuados = new ArrayList<>(this.hechos);
        } else {
            this.hechosConsensuados = algoritmoDeConsenso.aplicar(this.hechos, fuentes);
        }
    }

    public List<Hecho> getHechosCurados() {
        return this.hechosConsensuados;
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public void quitarFuentePorId(Long idFuente) {
        this.fuentes.removeIf(fuente -> fuente.getId().equals(idFuente));
    }

    @JsonProperty("tipoAlgoritmo")
    public String getTipoAlgoritmoAsString() {
        if (this.algoritmoDeConsenso == null) {
            return null;
        }
        return this.algoritmoDeConsenso.getClass().getSimpleName();
    }

}*/
