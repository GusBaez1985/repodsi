package algoritmosDeConsenso;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluadorDeConsensos {

    private final IColeccionRepository coleccionRepository;
    private final List<IHechoRepository> fuentes;

    public EvaluadorDeConsensos(IColeccionRepository coleccionRepository, List<IHechoRepository> fuentes) {
        this.coleccionRepository = coleccionRepository;
        this.fuentes = fuentes;
    }

    @Scheduled(cron = "0 0 3 * * *") // Todos los días a las 03:00 AM
    public void ejecutarEvaluacionDeConsensos() {
        List<Coleccion> colecciones = coleccionRepository.findAll();

        for (Coleccion coleccion : colecciones) {
            if (coleccion.getAlgoritmoDeConsenso() != null) {
                List<Hecho> hechosValidos = coleccion.getAlgoritmoDeConsenso().aplicar(coleccion.getHechos(), fuentes);
                coleccion.setHechosConsensuados(hechosValidos);
            } else {
                coleccion.setHechosConsensuados(coleccion.getHechos());
            }
        }
    }
}