package ar.utn.ba.ddsi.servicio_estadisticas.models.repositories;

import ar.utn.ba.ddsi.servicio_estadisticas.models.entities.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEstadisticasRepository extends JpaRepository<Estadistica, Long> {
    Optional<Estadistica> findTopByTipoOrderByValorDesc(String tipo);
    Optional<Estadistica> findTopByTipoAndClaveOrderByValorDesc(String tipo, String clave); //Desc(descendente)
}
