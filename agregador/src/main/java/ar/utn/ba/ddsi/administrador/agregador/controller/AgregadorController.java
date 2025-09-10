package ar.utn.ba.ddsi.administrador.agregador.controller;

import ar.utn.ba.ddsi.administrador.agregador.dto.*;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.administrador.agregador.services.ISolicitudEliminacionService;
import ar.utn.ba.ddsi.administrador.agregador.services.impl.ServicioRefrescoColecciones;
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO;
import ar.utn.ba.ddsi.administrador.service.factory.AlgoritmoDeConsensoFactory;
import ar.utn.ba.ddsi.administrador.service.factory.FuenteFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AgregadorController {

    private final IColeccionRepository coleccionRepository;
    private final IFuenteRepository fuenteRepository;
    private final ServicioRefrescoColecciones servicioRefresco;
    private final ISolicitudEliminacionService solicitudEliminacionService; // Unificada a un solo nombre
    private final ISolicitudEliminacionRepository solicitudRepository;


    public AgregadorController(
            IColeccionRepository coleccionRepository,
            IFuenteRepository fuenteRepository,
            ServicioRefrescoColecciones servicioRefresco,
            ISolicitudEliminacionService solicitudEliminacionService, // Unificada
            ISolicitudEliminacionRepository solicitudRepository) {

        this.coleccionRepository = coleccionRepository;
        this.fuenteRepository = fuenteRepository;
        this.servicioRefresco = servicioRefresco;
        this.solicitudEliminacionService = solicitudEliminacionService; // Unificada
        this.solicitudRepository = solicitudRepository;
    }

    @GetMapping("/colecciones")
    public List<ColeccionResponseDTO> listarColecciones() {
        List<Coleccion> colecciones = coleccionRepository.findAll();
        return colecciones.stream().map(coleccion -> {
            List<FuenteResponseDTO> fuentesDTO = coleccion.getFuentes().stream().map(fuente -> {
                String tipo = "desconocido";
                String nombre = "Sin Nombre";
                if (fuente instanceof FuenteContribuyente) {
                    tipo = "contribuyente";
                    nombre = "Aportes de Contribuyentes";
                } else if (fuente instanceof FuenteCargaManual) {
                    tipo = "manual";
                    nombre = "Carga Manual";
                } else if (fuente instanceof FuenteDataset) {
                    tipo = "dataset";
                    nombre = "Dataset Externo";
                }
                return new FuenteResponseDTO(fuente.getId(), tipo, nombre);
            }).collect(Collectors.toList());

            String tipoAlgoritmo = coleccion.getTipoAlgoritmo();

            return new ColeccionResponseDTO(
                    coleccion.getId(), coleccion.getTitulo(), coleccion.getDescripcion(), tipoAlgoritmo, fuentesDTO);
        }).collect(Collectors.toList());
    }
    @PostMapping("/colecciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Coleccion crearColeccion(@RequestBody ColeccionRequestDTO dto) {
        Coleccion nuevaColeccion = new Coleccion(dto.getTitulo(), dto.getDescripcion(), null);
        AlgoritmoDeConsenso algoritmo = AlgoritmoDeConsensoFactory.crear(dto.getTipoAlgoritmo());
        nuevaColeccion.setAlgoritmoDeConsenso(algoritmo);
        nuevaColeccion.setTipoAlgoritmo(dto.getTipoAlgoritmo());
        return coleccionRepository.save(nuevaColeccion);
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Coleccion> obtenerColeccion(@PathVariable Long id) {
        return ResponseEntity.of(coleccionRepository.findById(id));
    }


    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con id: " + id));

        Fuente nuevaFuente = FuenteFactory.crear(dto);
        coleccion.agregarFuente(nuevaFuente);
        coleccionRepository.save(coleccion);

        System.out.println("[AGREGADOR DEBUG] Fuente del tipo '" + dto.getTipo() + "' fue agregada a la Colección ID " + id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/colecciones/{id}/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechosDeColeccion(@PathVariable("id") Long id) {
        return ResponseEntity.of(coleccionRepository.findById(id).map(Coleccion::getHechos));
    }

    @PostMapping("/colecciones/{idColeccion}/fuentes/{idFuente}/procesar")
    public ResponseEntity<String> procesarFuenteDeColeccion(@PathVariable("idColeccion") Long idColeccion, @PathVariable("idFuente") Long idFuente) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con id: " + idColeccion));
        Fuente fuente = fuenteRepository.findById(idFuente)
                .orElseThrow(() -> new RuntimeException("Fuente no encontrada con id: " + idFuente));

        if (fuente instanceof FuenteDataset) {
            servicioRefresco.procesarFuenteEstatica(coleccion, (FuenteDataset) fuente);
            return ResponseEntity.ok("Procesamiento de la fuente dataset iniciado.");
        } else {
            return ResponseEntity.badRequest().body("La fuente no es de tipo 'dataset'.");
        }
    }

    @PostMapping("/solicitudes-eliminacion")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearSolicitud(@RequestBody SolicitudRequestDTO dto) {
        solicitudEliminacionService.crearSolicitud(dto); // Usamos el nombre unificado
    }


    @GetMapping("/solicitudes-eliminacion")
    public List<SolicitudEliminacion> listarSolicitudesDeEliminacion() {
        return solicitudRepository.findAll();
    }

    @GetMapping("/solicitudes-eliminacion/spam")
    public List<SolicitudEliminacion> listarSolicitudesSpam() {
        return solicitudEliminacionService.obtenerSolicitudesSpam(); // Usamos el nombre unificado
    }

    @GetMapping("/solicitudes-eliminacion/estadisticas-spam")
    public EstadisticasSpamDTO getEstadisticasSpam() {
        return solicitudEliminacionService.obtenerEstadisticasSpam(); // Usamos el nombre unificado
    }
}