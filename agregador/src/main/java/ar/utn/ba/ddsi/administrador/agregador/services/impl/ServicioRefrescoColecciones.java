package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.IServicioRefrescoColecciones;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioRefrescoColecciones implements IServicioRefrescoColecciones {

    private final IHechoRepository fuenteDinamica;
    private final IHechoRepository fuenteEstatica;

    public ServicioRefrescoColecciones(
            @Qualifier("fuenteDinamica") IHechoRepository fuenteDinamica,
            @Qualifier("fuenteEstatica") IHechoRepository fuenteEstatica) {

        this.fuenteDinamica = fuenteDinamica;
        this.fuenteEstatica = fuenteEstatica;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 * * * *") // cada una hora
    public void refrescarColecciones() {
        refrescarFuente(fuenteDinamica);
        refrescarFuente(fuenteEstatica);
    }

    private void refrescarFuente(IHechoRepository fuente) {
        List<Hecho> hechos = fuente.findAll();

        hechos.forEach(hecho -> {
            fuente.save(hecho);
        });

        System.out.println("Refrescada fuente: " + fuente.getClass().getSimpleName());
    }
}
