package ar.edu.utn.frba.dds.domain.contribuyente;

import ar.edu.utn.frba.dds.domain.coleccion.Coleccion;
import ar.edu.utn.frba.dds.domain.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.domain.coleccion.Hecho;
import ar.edu.utn.frba.dds.domain.coleccion.SolicitudEliminacion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Contribuyente {
    private String nombre;
    private String apellido;
    private Integer edad;

    public Contribuyente() {
        this.nombre = "";
        this.apellido = "";
        this.edad = 0;
    }

    public Contribuyente(String nombre) {
        this.nombre = nombre;
        this.apellido = "";
        this.edad = 0;
    }

    public Contribuyente(String nombre, String apellido, Integer edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    public List<Hecho> obtenerHechos(Coleccion coleccion, List<CriterioDePertenencia> filtros){

        List<Hecho> hechos = coleccion.getHechos();

        if (filtros.isEmpty()) {
            return hechos;
        }

        return hechos.stream()
                .filter(hecho -> filtros.stream()
                        .allMatch(filtro -> filtro.cumpleCriterio(hecho))).toList();
    }

    public SolicitudEliminacion realizarSolicitudEliminacion(Hecho hecho, String motivo){
        SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion(motivo, hecho);
        return nuevaSolicitudEliminacion;
    }
}
