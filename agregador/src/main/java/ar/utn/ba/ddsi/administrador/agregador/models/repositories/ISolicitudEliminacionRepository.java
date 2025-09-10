package ar.utn.ba.ddsi.administrador.agregador.models.repositories;
import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
    long countByEstadoSolicitud(EstadoSolicitud estado);
    /*
	public List<SolicitudEliminacion> findAll();
	public SolicitudEliminacion save(SolicitudEliminacion solicitud);
	public void delete(SolicitudEliminacion solicitud);*/
}

