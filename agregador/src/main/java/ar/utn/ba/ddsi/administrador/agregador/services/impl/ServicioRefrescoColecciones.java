package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.IServicioRefrescoColecciones;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // <--- Importamos el cliente HTTP
import java.util.Arrays;

@Service
public class ServicioRefrescoColecciones implements IServicioRefrescoColecciones {

    // El repositorio CENTRAL de hechos, donde guardaremos todo lo que recolectemos.
    private final IHechoRepository hechoRepositoryCentral;
    // El cliente HTTP para llamar a otros servicios.
    private final RestTemplate restTemplate;

    // Modificamos el constructor para que solo necesite el repositorio central.
    public ServicioRefrescoColecciones(IHechoRepository hechoRepository) {
        this.hechoRepositoryCentral = hechoRepository;
        this.restTemplate = new RestTemplate(); // Creamos una instancia del cliente HTTP.
    }

    @PostConstruct
    @Scheduled(cron = "0 */5 * * * *") // Cada 5 minutos
    public void refrescarColecciones() {
        System.out.println("--- [AGREGADOR] Iniciando proceso de refresco de fuentes ---");
        refrescarFuenteDinamica();
        // Aquí podrías agregar llamadas para refrescar otras fuentes (proxy, estática, etc.)
    }

    private void refrescarFuenteDinamica() {
        String urlFuenteDinamica = "http://localhost:8084/hechos"; // URL del servicio que expusimos en el Paso 1
        try {
            // 1. Hacemos la llamada HTTP GET para traer los hechos de la fuente dinámica.
            Hecho[] hechosNuevos = restTemplate.getForObject(urlFuenteDinamica, Hecho[].class);

            // 2. Si la respuesta no es nula y contiene hechos, los procesamos.
            if (hechosNuevos != null && hechosNuevos.length > 0) {
                // 3. Guardamos cada hecho nuevo en nuestro repositorio CENTRAL.
                Arrays.stream(hechosNuevos).forEach(hechoRepositoryCentral::save);
                System.out.println("--- [AGREGADOR] Se importaron " + hechosNuevos.length + " hechos desde la Fuente Dinámica.");
            } else {
                System.out.println("--- [AGREGADOR] No se encontraron hechos nuevos en la Fuente Dinámica.");
            }
        } catch (Exception e) {
            System.err.println("--- [AGREGADOR] ERROR al contactar la Fuente Dinámica en " + urlFuenteDinamica + ": " + e.getMessage());
        }
    }
}








/*package ar.utn.ba.ddsi.administrador.agregador.services.impl;

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

        hechos.forEach(fuente::save);

        System.out.println("Refrescada fuente: " + fuente.getClass().getSimpleName());
    }
}
*/