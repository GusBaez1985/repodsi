package ar.edu.utn.frba.dds.domain.administrador;

import ar.edu.utn.frba.dds.domain.coleccion.Coleccion;
import ar.edu.utn.frba.dds.domain.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.domain.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.domain.coleccion.SolicitudEliminacion;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Administrador {
    private List<SolicitudEliminacion> listadoDeSolicitudes;

    public Administrador() {
        this.listadoDeSolicitudes = new ArrayList<>();
    }

    public Coleccion crearColeccion(String titulo, String descripcion, CriterioDePertenencia criterio){
        return new Coleccion(titulo, descripcion, criterio);
    }

    public void aprobarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {

        if (solicitudEliminacion.getHecho().getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        solicitudEliminacion.getHecho().setEliminado(true);
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.APROBADA);
    }

    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitudEliminacion) {

        if (solicitudEliminacion.getHecho().getEliminado()) {
            throw new IllegalStateException("El hecho ya se encuentra eliminado.");
        }

        solicitudEliminacion.getHecho().setEliminado(false);
        solicitudEliminacion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
    }
}