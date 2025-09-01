package ar.utn.ba.ddsi.administrador.service;

import ar.utn.ba.ddsi.administrador.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorService {

    private final RestTemplate restTemplate = new RestTemplate();
    // La URL base del servicio agregador (asumiendo que corre en el puerto 8081)
    private final String AGREGADOR_API_URL = "http://localhost:8081";

    // --- Métodos de Colecciones ---

    public List<ColeccionResponseDTO> obtenerTodasLasColecciones() {
        String url = AGREGADOR_API_URL + "/colecciones";
        ColeccionResponseDTO[] respuesta = restTemplate.getForObject(url, ColeccionResponseDTO[].class);
        if (respuesta == null) {
            return Collections.emptyList(); // Devuelve lista vacía si no hay nada
        }
        return Arrays.stream(respuesta).collect(Collectors.toList());
    }

    public ColeccionResponseDTO crearColeccion(ColeccionDTO dto) {
        String url = AGREGADOR_API_URL + "/colecciones";
        return restTemplate.postForObject(url, dto, ColeccionResponseDTO.class);
    }

    public ColeccionResponseDTO modificarColeccion(Long id, ColeccionDTO dto) {
        String url = AGREGADOR_API_URL + "/colecciones/" + id;
        restTemplate.put(url, dto);
        // Hacemos un get para devolver el objeto actualizado, ya que PUT no devuelve contenido.
        return restTemplate.getForObject(url, ColeccionResponseDTO.class);
    }

    public void eliminarColeccion(Long id) {
        String url = AGREGADOR_API_URL + "/colecciones/" + id;
        restTemplate.delete(url);
    }

    // --- Métodos de Hechos ---

    public List<HechoResponseDTO> obtenerHechosDeColeccion(Long idColeccion) {
        String url = AGREGADOR_API_URL + "/colecciones/" + idColeccion + "/hechos";
        HechoResponseDTO[] respuesta = restTemplate.getForObject(url, HechoResponseDTO[].class);
        if (respuesta == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(respuesta).collect(Collectors.toList());
    }

    // --- Métodos de Algoritmos y Fuentes ---

    public void modificarAlgoritmoDeConsenso(Long idColeccion, String tipoAlgoritmo) {
        String url = AGREGADOR_API_URL + "/colecciones/" + idColeccion + "/algoritmo";
        // Creamos el DTO que espera el endpoint del agregador
        ModificarAlgoritmoDTO body = new ModificarAlgoritmoDTO(tipoAlgoritmo);
        restTemplate.put(url, body);
    }

    public void agregarFuente(Long idColeccion, FuenteDTO dto) {
        String url = AGREGADOR_API_URL + "/colecciones/" + idColeccion + "/fuentes";
        restTemplate.postForObject(url, dto, Void.class);
    }

    public void quitarFuente(Long idColeccion, Long idFuente) {
        String url = AGREGADOR_API_URL + "/colecciones/" + idColeccion + "/fuentes/" + idFuente;
        restTemplate.delete(url);
    }

    // --- Métodos de Solicitudes (Completados) ---

    public void aprobarSolicitud(Long idSolicitud) {
        String url = AGREGADOR_API_URL + "/solicitudes/" + idSolicitud + "/aprobar";
        restTemplate.postForObject(url, null, Void.class);
    }

    public void rechazarSolicitud(Long idSolicitud) {
        String url = AGREGADOR_API_URL + "/solicitudes/" + idSolicitud + "/rechazar";
        restTemplate.postForObject(url, null, Void.class);
    }
}

/*





package ar.utn.ba.ddsi.administrador.service;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.administrador.dto.ColeccionDTO;
import ar.utn.ba.ddsi.administrador.dto.ColeccionResponseDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO;
import ar.utn.ba.ddsi.administrador.service.factory.AlgoritmoDeConsensoFactory;
import ar.utn.ba.ddsi.administrador.service.factory.FuenteFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final IColeccionRepository coleccionRepository;
    private final IFuenteRepository fuenteRepository;

    public AdministradorService(IColeccionRepository coleccionRepository, IFuenteRepository fuenteRepository) {
        this.coleccionRepository = coleccionRepository;
        this.fuenteRepository = fuenteRepository; // Lo asignamos al atributo
    }

    // GET
    public List<ColeccionResponseDTO> obtenerTodasLasColecciones() {
        return coleccionRepository.findAll()
                .stream()
                .map(ColeccionResponseDTO::from)
                .toList();
    }
    // PuT
    public Coleccion crearColeccion(ColeccionDTO dto) {
        CriterioDePertenencia criterio = new CriterioDummy();  // TEMPORAL

        Coleccion coleccion = new Coleccion(dto.getTitulo(), dto.getDescripcion(), criterio);

        AlgoritmoDeConsenso algoritmo = AlgoritmoDeConsensoFactory.crear(dto.getTipoAlgoritmo());
        coleccion.setAlgoritmoDeConsenso(algoritmo); // La factory ya se encarga de devolver null si el tipo es nulo.

        this.coleccionRepository.save(coleccion);
        return coleccion;
    }

    // PUT
    public Coleccion modificarColeccion(Long id, ColeccionDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(id);

        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + id);
        }

        coleccion.setTitulo(dto.getTitulo());
        coleccion.setDescripcion(dto.getDescripcion());

        coleccionRepository.save(coleccion);
        return coleccion;
    }

    public void eliminarColeccion(Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);

        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + id);
        }

        coleccionRepository.delete(coleccion);
    }


    public List<Hecho> obtenerHechosDeColeccion(Long idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) throw new RuntimeException("Colección no encontrada");
        return coleccion.getHechosCurados();
    }

    public void modificarAlgoritmoDeConsenso(Long idColeccion, String tipoAlgoritmo) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada");
        }
        AlgoritmoDeConsenso algoritmo = AlgoritmoDeConsensoFactory.crear(tipoAlgoritmo);

        coleccion.setAlgoritmoDeConsenso(algoritmo);
        coleccionRepository.save(coleccion);
    }

    public void agregarFuente(Long idColeccion, FuenteDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + idColeccion);
        }

        Fuente nuevaFuente = FuenteFactory.crear(dto);

        this.fuenteRepository.save(nuevaFuente);

        coleccion.agregarFuente(nuevaFuente);
        coleccionRepository.save(coleccion);
    }


    public void quitarFuente(Long idColeccion, Long idFuente) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + idColeccion);
        }
        // 1. Buscamos si la fuente existe DENTRO de la colección.
        boolean fuenteExistia = coleccion.getFuentes().stream()
                .anyMatch(fuente -> fuente.getId().equals(idFuente));

        // 2. Si no existía, lanzamos un error claro.
        if (!fuenteExistia) {
            throw new RuntimeException("La fuente con ID: " + idFuente + " no existe en esta colección.");
        }

        // 3. Si existía, procedemos a quitarla.
        coleccion.quitarFuentePorId(idFuente);
        coleccionRepository.save(coleccion);
    }

}
*/