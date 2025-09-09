package ar.edu.utn.frba.dds.models.entities.administrador;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "administrador")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "administrador")
    private List<SolicitudEliminacion> listadoDeSolicitudes;

    public Administrador() {
        this.listadoDeSolicitudes = new ArrayList<>();
    }

    public Coleccion crearColeccion(String titulo, String descripcion, CriterioDePertenencia criterio){
        return new Coleccion(titulo, descripcion, criterio);
    }

    public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {

        /*
        if (solicitudEliminacion.getHecho().getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        solicitudEliminacion.getHecho().setEliminado(true); */

        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
        solicitudEliminacion.setAdministrador(this);
        solicitudEliminacion.setFechaResolucion(LocalDateTime.now());
    }

    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {

        /*
        if (solicitudEliminacion.getHecho().getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        solicitudEliminacion.getHecho().setEliminado(false); */
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
        solicitudEliminacion.setAdministrador(this);
        solicitudEliminacion.setFechaResolucion(LocalDateTime.now());
    }
}