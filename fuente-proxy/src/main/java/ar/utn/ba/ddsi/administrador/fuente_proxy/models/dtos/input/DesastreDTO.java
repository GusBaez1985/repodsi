package ar.utn.ba.ddsi.administrador.fuente_proxy.models.dtos.input;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class DesastreDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private OffsetDateTime fecha_hecho;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;
}
