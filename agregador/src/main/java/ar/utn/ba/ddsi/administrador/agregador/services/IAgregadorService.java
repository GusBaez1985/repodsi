package ar.utn.ba.ddsi.administrador.agregador.services;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import java.util.List;

public interface IAgregadorService {
	public List<Hecho> obtenerTodosLosHechos();
	public List<Hecho> obtenerHechosPorCategoria(String categoria);
	public void procesarSolicitudEliminacion(SolicitudEliminacion solicitud);
}
