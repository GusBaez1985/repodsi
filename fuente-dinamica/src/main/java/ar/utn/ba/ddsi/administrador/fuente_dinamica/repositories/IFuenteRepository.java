package ar.utn.ba.ddsi.administrador.fuente_dinamica.repositories;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
}