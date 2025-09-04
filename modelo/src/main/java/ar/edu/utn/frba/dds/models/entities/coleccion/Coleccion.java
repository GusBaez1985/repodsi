package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Coleccion {
    private Long id;
    private String titulo;
    private String descripcion;
    private CriterioDePertenencia criterio;
    private List<Hecho> hechos;
    @JsonIgnore
    private AlgoritmoDeConsenso algoritmoDeConsenso; // puede ser null


    private List<Hecho> hechosConsensuados;
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
    /**
     * Este método es utilizado por Jackson durante la serialización a JSON.
     * Devuelve el nombre simple de la clase del algoritmo, que coincide
     * con el "tipoAlgoritmo" que esperan los DTOs.
     * @return El nombre del tipo de algoritmo como un String.
     */
    @JsonProperty("tipoAlgoritmo")
    public String getTipoAlgoritmoAsString() {
        if (this.algoritmoDeConsenso == null) {
            return null;
        }
        return this.algoritmoDeConsenso.getClass().getSimpleName();
    }

}
