package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import ar.edu.utn.frba.dds.models.entities.lectorCSV.LectorDeHechosCSV;
import ar.utn.ba.ddsi.administrador.agregador.dto.HechoFuenteDinamicaDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.IServicioRefrescoColecciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;

@Service
public class ServicioRefrescoColecciones implements IServicioRefrescoColecciones {
    private static final Logger logger = LoggerFactory.getLogger(ServicioRefrescoColecciones.class);

    private final IHechoRepository hechoRepositoryCentral;
    private final IColeccionRepository coleccionRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ServicioRefrescoColecciones(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository) {
        this.hechoRepositoryCentral = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.restTemplate = new RestTemplate();
    }


    //@PostConstruct
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
                // --- INICIO DE LA SOLUCIÓN ÓPTIMA ---

                List<Hecho> hechosParaAgregar = Arrays.stream(hechosRecibidos)
                        .map(dto -> {
                            // 1. Creamos el objeto Hecho a partir del DTO.
                            Hecho hecho = Hecho.builder()
                                    // .id(dto.getId()) // <-- YA NO CONFIAMOS EN EL ID DE LA FUENTE
                                    .titulo(dto.getTitulo())
                                    .descripcion(dto.getDescripcion())
                                    .categoria(dto.getCategoria())
                                    .ubicacion(new Ubicacion(dto.getLatitud(), dto.getLongitud()))
                                    .fecAcontecimiento(dto.getFechaAcontecimiento())
                                    .etiquetas(dto.getEtiquetas())
                                    .build();

                            // 2. IMPORTANTE: Forzamos el ID a ser null.
                            // Esto obliga a nuestro HechoRepository a asignarle un ID nuevo
                            // desde SU PROPIA secuencia, centralizando la lógica.
                            hecho.setId(null);

                            return hecho;
                        })
                        // 3. Ahora el filtro anti-duplicados es más complejo.
                        // No podemos buscar por ID, debemos buscar por contenido.
                        // Este es un ejemplo simple, se puede hacer más robusto.
                        .filter(hechoNuevo ->
                                hechoRepositoryCentral.findAll().stream().noneMatch(hechoExistente ->
                                        hechoExistente.getTitulo().equals(hechoNuevo.getTitulo()) &&
                                                hechoExistente.getDescripcion().equals(hechoNuevo.getDescripcion())
                                )
                        )
                        .toList();


                if (!hechosParaAgregar.isEmpty()) {
                    hechoRepositoryCentral.saveAll(hechosParaAgregar);

                    // IMPORTANTE: Asociar los nuevos hechos SOLO a las colecciones
                    // que tengan definida una FuenteContribuyente.
                    List<Coleccion> coleccionesParaActualizar = coleccionRepository.findAll().stream()
                            .filter(coleccion -> coleccion.getFuentes().stream()
                                    .anyMatch(fuente -> fuente instanceof FuenteContribuyente))
                            .toList();

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
            // Usamos el logger para registrar el error de forma estructurada.
            logger.error("--- [AGREGADOR] ERROR durante el refresco de la Fuente Dinámica: {}", e.getMessage(), e);
        }
    }
    public void procesarFuenteEstatica(Coleccion coleccion, FuenteDataset fuenteDataset) {
        logger.info("[AGREGADOR] Iniciando procesamiento de Fuente Estática ID {} para Colección ID {}", fuenteDataset.getId(), coleccion.getId());
        try {

            String pathCompleto = Paths.get("agregador", "data", fuenteDataset.getPath()).toString();
            FileReader fileReader = new FileReader(pathCompleto);
            LectorDeHechosCSV lector = new LectorDeHechosCSV(fileReader);
            List<Hecho> hechosDelCsv = lector.obtenerHechos(pathCompleto, fuenteDataset);



            // --- LÓGICA DE FILTRADO CORREGIDA (sin usar el ID nulo) ---
            List<Hecho> hechosExistentes = hechoRepositoryCentral.findAll();

            List<Hecho> hechosParaAgregar = hechosDelCsv.stream()
                    .filter(hechoDelCsv ->
                            hechosExistentes.stream().noneMatch(hechoExistente ->
                                    hechoExistente.getTitulo().equals(hechoDelCsv.getTitulo()) &&
                                            hechoExistente.getFecAcontecimiento().equals(hechoDelCsv.getFecAcontecimiento())
                            )
                    )
                    .toList();

            if (!hechosParaAgregar.isEmpty()) {
                for (Hecho hechoNuevo : hechosParaAgregar) {
                    // Como el hecho del CSV no tiene fuente, se la asignamos aquí.
                    hechoNuevo.setFuente(fuenteDataset);
                    Hecho hechoGuardado = hechoRepositoryCentral.save(hechoNuevo);
                    coleccion.agregarHecho(hechoGuardado);
                }
                coleccionRepository.save(coleccion);
                logger.info("[AGREGADOR] Se importaron y asignaron {} hechos desde el CSV: {}", hechosParaAgregar.size(), fuenteDataset.getPath());
            } else {
                logger.info("[AGREGADOR] No se encontraron hechos nuevos en el CSV o ya existían.");
            }

        } catch (Exception e) {
            logger.error("--- [AGREGADOR] ERROR procesando Fuente Estática: {}", e.getMessage(), e);
        }
    }

}
