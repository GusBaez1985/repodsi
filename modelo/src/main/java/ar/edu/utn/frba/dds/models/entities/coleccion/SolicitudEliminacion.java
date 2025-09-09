package ar.edu.utn.frba.dds.models.entities.coleccion;

import ar.edu.utn.frba.dds.models.entities.administrador.Administrador;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motivo")
    private String motivo;

    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoSolicitud;

    @ManyToOne
    @JoinColumn(name = "hecho_id", nullable = false)
    private Hecho hecho;

    @ManyToOne
    @JoinColumn(name = "contribuyente_id", nullable = false)
    private Contribuyente contribuyente; //El creador de la solicitud

    @Setter
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Administrador administrador; //El que revisa la solicitud y cambia de estado

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Setter
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public SolicitudEliminacion(String motivo, Hecho hecho, Contribuyente contribuyente) {
        this.motivo = motivo;
        this.estadoSolicitud = EstadoSolicitud.SIN_REVISAR;
        this.hecho = hecho;
        this.contribuyente = contribuyente;
        this.fechaCreacion = LocalDateTime.now();
    }

}