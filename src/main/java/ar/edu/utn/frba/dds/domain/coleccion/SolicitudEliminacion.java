package ar.edu.utn.frba.dds.domain.coleccion;

import ar.edu.utn.frba.dds.domain.criterios.CriterioAprobacionSolicitud;
import ar.edu.utn.frba.dds.domain.criterios.CriterioFundamentacionMinima;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SolicitudEliminacion {
    private String motivo;
    private List<CriterioAprobacionSolicitud> criteriosSolicitudEliminacion;
    @Setter
    private EstadoSolicitud estadoSolicitud;
    private Hecho hecho;

    public SolicitudEliminacion(String motivo, Hecho hecho) {
        this.motivo = motivo;
        this.criteriosSolicitudEliminacion = new ArrayList<>();
        this.getCriteriosSolicitudEliminacion().add(new CriterioFundamentacionMinima());
        this.estadoSolicitud = EstadoSolicitud.SIN_REVISAR;
        this.hecho = hecho;
    }

    public boolean esSolicitudValida() {
        for (CriterioAprobacionSolicitud criterio : criteriosSolicitudEliminacion) {
            if (!criterio.cumpleCriterio(this.motivo)) return false;
        }
        return true;
    }
}