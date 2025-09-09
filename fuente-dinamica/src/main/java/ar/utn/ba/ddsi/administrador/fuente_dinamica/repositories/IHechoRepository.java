package ar.utn.ba.ddsi.administrador.fuente_dinamica.repositories;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    // Spring Data JPA implementa los métodos por nosotros.
}