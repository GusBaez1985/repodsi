package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion; // <-- Importante
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.utn.ba.ddsi.administrador.agregador.dto.HechoFuenteDinamicaDTO; // <-- Importante
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.IServicioRefrescoColecciones;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioRefrescoColecciones implements IServicioRefrescoColecciones {

    private final IHechoRepository hechoRepositoryCentral;
    private final IColeccionRepository coleccionRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ServicioRefrescoColecciones(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository) {
        this.hechoRepositoryCentral = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.restTemplate = new RestTemplate();
    }


    @PostConstruct
    @Scheduled(cron = "0 */5 * * * *")
    public void refrescarColecciones() {
        System.out.println("--- [AGREGADOR] Iniciando proceso de refresco de fuentes dinámicas ---");
        // La única lógica que debe ejecutar el cron es la de la fuente dinámica.
        // Las fuentes estáticas (CSV) se procesan bajo demanda, no periódicamente.
        refrescarFuenteDinamica();
    }


    private void refrescarFuenteDinamica() {
        // La URL de la fuente dinámica es una configuración del propio módulo agregador,
        // ya que siempre se conectará al mismo endpoint de "fuente-dinamica".
        String urlFuenteDinamica = "http://localhost:8084/hechos";
        try {
            HechoFuenteDinamicaDTO[] hechosRecibidos = restTemplate.getForObject(urlFuenteDinamica, HechoFuenteDinamicaDTO[].class);

            if (hechosRecibidos != null && hechosRecibidos.length > 0) {

                // --- CÓDIGO CON LA LÓGICA ANTI-DUPLICACIÓN QUE YA FUNCIONA ---
                List<Hecho> hechosParaAgregar = Arrays.stream(hechosRecibidos).map(dto ->
                                Hecho.builder()
                                        .id(dto.getId())
                                        .titulo(dto.getTitulo())
                                        .descripcion(dto.getDescripcion())
                                        .categoria(dto.getCategoria())
                                        .ubicacion(new Ubicacion(dto.getLatitud(), dto.getLongitud()))
                                        .fecAcontecimiento(dto.getFechaAcontecimiento())
                                        .etiquetas(dto.getEtiquetas())
                                        .build()
                        ).filter(hecho -> hechoRepositoryCentral.findById(hecho.getId()).isEmpty())
                        .collect(Collectors.toList());

                if (!hechosParaAgregar.isEmpty()) {
                    hechosParaAgregar.forEach(hechoRepositoryCentral::save);

                    // IMPORTANTE: Asociar los nuevos hechos SÓLO a las colecciones
                    // que tengan definida una FuenteContribuyente.
                    List<Coleccion> coleccionesParaActualizar = coleccionRepository.findAll().stream()
                            .filter(coleccion -> coleccion.getFuentes().stream()
                                    .anyMatch(fuente -> fuente instanceof FuenteContribuyente))
                            .collect(Collectors.toList());

                    coleccionesParaActualizar.forEach(coleccion -> {
                        hechosParaAgregar.forEach(coleccion::agregarHecho);
                        coleccionRepository.save(coleccion);
                    });

                    System.out.println("--- [AGREGADOR] Se importaron y asignaron " + hechosParaAgregar.size() + " hechos nuevos desde la Fuente Dinámica.");
                } else {
                    System.out.println("--- [AGREGADOR] No se encontraron hechos nuevos en la Fuente Dinámica.");
                }
            }
        } catch (Exception e) {
            System.err.println("--- [AGREGADOR] ERROR durante el refresco de la Fuente Dinámica:");
            e.printStackTrace();
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