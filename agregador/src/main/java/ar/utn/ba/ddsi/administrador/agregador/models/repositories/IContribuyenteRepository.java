package ar.utn.ba.ddsi.administrador.agregador.models.repositories;

import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContribuyenteRepository extends JpaRepository<Contribuyente, Long> {
}