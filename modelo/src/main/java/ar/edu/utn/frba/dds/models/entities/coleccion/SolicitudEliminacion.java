package ar.edu.utn.frba.dds.models.entities.coleccion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SolicitudEliminacion {
    @Setter
    private Long id;
    private String motivo;
    @Setter
    private EstadoSolicitud estadoSolicitud;
    private Long idHecho;
    private Long idContribuyente; //El creador de la solicitud
    @Setter
    private Long idAdministrador; //El que revisa la solicitud y cambia de estado
    private LocalDateTime fechaCreacion;
    @Setter
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion(String motivo, Long idHecho, Long idContribuyente) {
        this.motivo = motivo;
        this.estadoSolicitud = EstadoSolicitud.SIN_REVISAR;
        this.idHecho = idHecho;
        this.idContribuyente = idContribuyente;
        this.fechaCreacion = LocalDateTime.now();
    }
}