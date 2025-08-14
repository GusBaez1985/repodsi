package ar.utn.ba.ddsi.fuente_dinamica.dtos;

import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoRevision;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoRevisionDTO {
	private EstadoRevision estado;
	private String comentario;
}