package ar.utn.ba.ddsi.administrador.fuente_dinamica.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
    // También lo dejamos vacío.
}