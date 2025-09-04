package ar.utn.ba.ddsi.administrador.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion; // <-- Importante
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import ar.edu.utn.frba.dds.models.entities.lectorCSV.LectorDeHechosCSV;
import ar.utn.ba.ddsi.administrador.agregador.dto.HechoFuenteDinamicaDTO; // <-- Importante
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.IServicioRefrescoColecciones;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.nio.file.Paths;
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
                        .toList();

                if (!hechosParaAgregar.isEmpty()) {
                    hechosParaAgregar.forEach(hechoRepositoryCentral::save);

                    // IMPORTANTE: Asociar los nuevos hechos SÓLO a las colecciones
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
            System.err.println("--- [AGREGADOR] ERROR durante el refresco de la Fuente Dinámica:");
            e.printStackTrace();
        }
    }



    public void procesarFuenteEstatica(Coleccion coleccion, FuenteDataset fuenteDataset) {
        System.out.println("[AGREGADOR] Iniciando procesamiento de Fuente Estática ID " + fuenteDataset.getId() + " para Colección ID " + coleccion.getId());
        try {
            String pathCompleto = Paths.get("agregador", "data", fuenteDataset.getPath()).toString();

            // --- INICIO DE CAMBIO ---

            // 1. Creamos el FileReader que el constructor de LectorDeHechosCSV necesita.
            FileReader fileReader = new FileReader(pathCompleto);

            // 2. Ahora sí, creamos la instancia del lector pasándole el fileReader.
            LectorDeHechosCSV lector = new LectorDeHechosCSV(fileReader);

            // 3. Llamamos al método obtenerHechos con los dos argumentos que requiere.
            List<Hecho> hechosDelCsv = lector.obtenerHechos(pathCompleto, fuenteDataset);

            // --- FIN DE CAMBIO ---

            List<Hecho> hechosParaAgregar = hechosDelCsv.stream()
                    .filter(hecho -> hechoRepositoryCentral.findById(hecho.getId()).isEmpty())
                    .toList();

            if (!hechosParaAgregar.isEmpty()) {
                hechosParaAgregar.forEach(hechoRepositoryCentral::save);
                hechosParaAgregar.forEach(coleccion::agregarHecho);
                coleccionRepository.save(coleccion);
                System.out.println("[AGREGADOR] Se importaron y asignaron " + hechosParaAgregar.size() + " hechos desde el CSV: " + fuenteDataset.getPath());
            } else {
                System.out.println("[AGREGADOR] No se encontraron hechos nuevos en el CSV o ya existían.");
            }

        } catch (Exception e) {
            System.err.println("--- [AGREGADOR] ERROR procesando Fuente Estática: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
