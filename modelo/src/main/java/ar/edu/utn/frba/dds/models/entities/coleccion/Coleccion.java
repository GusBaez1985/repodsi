package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
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
    private AlgoritmoDeConsenso algoritmoDeConsenso; // puede ser null
    private List<Hecho> hechosConsensuados;

    public Coleccion(String titulo, String descripcion, CriterioDePertenencia criterio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterio = criterio;
        this.hechos = new ArrayList<>();
        this.algoritmoDeConsenso = null;
        this.hechosConsensuados = new ArrayList<>();
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
}
