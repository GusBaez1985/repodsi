package ar.utn.ba.ddsi.servicio_estadisticas.services.impl;

import ar.utn.ba.ddsi.servicio_estadisticas.models.repositories.IEstadisticasRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasScheduler {

    private final IEstadisticasRepository repository;
    private final ServicioEstadisticasService service;

    public EstadisticasScheduler(IEstadisticasRepository repository, ServicioEstadisticasService service) {
        this.repository = repository;
        this.service = service;
    }

    //Corre todos los días a la medianoche
    @Scheduled(cron = "0 0 0 * * ?")
    public void recalcularEstadisticas() {
        repository.deleteAll(); //Borro cache anterior
        repository.saveAll(service.calcularEstadisticas()); //Recalculo y guardo
    }
}
