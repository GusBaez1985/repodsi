package ar.utn.ba.ddsi.administrador.service;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.administrador.dto.*;
import ar.utn.ba.ddsi.administrador.service.factory.AlgoritmoDeConsensoFactory;
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
        String url = AGREGADOR_API_URL + "/api/colecciones";
        ColeccionResponseDTO[] respuesta = restTemplate.getForObject(url, ColeccionResponseDTO[].class);
        if (respuesta == null) {
            return Collections.emptyList(); // Devuelve lista vacía si no hay nada
        }
        return Arrays.stream(respuesta).collect(Collectors.toList());
    }


    public ColeccionResponseDTO crearColeccion(ColeccionDTO dto) {
        Coleccion coleccion = new Coleccion();
        coleccion.setTitulo(dto.getTitulo());
        coleccion.setDescripcion(dto.getDescripcion());

        coleccion.setTipoAlgoritmo(dto.getTipoAlgoritmo());

        coleccion.setAlgoritmoDeConsenso(AlgoritmoDeConsensoFactory.crear(dto.getTipoAlgoritmo()));

        // La URL correcta ya la tenías.
        String url = AGREGADOR_API_URL + "/api/colecciones";
        Coleccion nuevaColeccion = restTemplate.postForObject(url, coleccion, Coleccion.class);

        return ColeccionResponseDTO.from(nuevaColeccion);
    }

    public ColeccionResponseDTO modificarColeccion(Long id, ColeccionDTO dto) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + id;
        restTemplate.put(url, dto);
        // Hacemos un get para devolver el objeto actualizado, ya que PUT no devuelve contenido.
        return restTemplate.getForObject(url, ColeccionResponseDTO.class);
    }

    public void eliminarColeccion(Long id) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + id;
        restTemplate.delete(url);
    }

    // --- Métodos de Hechos ---

    public List<HechoResponseDTO> obtenerHechosDeColeccion(Long idColeccion) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + idColeccion + "/hechos";

        // convertir la respuesta a un array de DTOs directamente y sin errores.
        HechoResponseDTO[] respuesta = restTemplate.getForObject(url, HechoResponseDTO[].class);

        if (respuesta == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(respuesta);
    }
    // --- Métodos de Algoritmos y Fuentes ---

    public void modificarAlgoritmoDeConsenso(Long idColeccion, String tipoAlgoritmo) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + idColeccion + "/algoritmo";
        // Creamos el DTO que espera el endpoint del agregador
        ModificarAlgoritmoDTO body = new ModificarAlgoritmoDTO(tipoAlgoritmo);
        restTemplate.put(url, body);
    }

    public void agregarFuente(Long idColeccion, FuenteDTO dto) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + idColeccion + "/fuentes";
        restTemplate.postForObject(url, dto, Void.class);
    }

    public void quitarFuente(Long idColeccion, Long idFuente) {
        String url = AGREGADOR_API_URL + "/api/colecciones/" + idColeccion + "/fuentes/" + idFuente;
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
    public void procesarFuenteDeColeccion(Long idColeccion, Long idFuente) {
        // Construimos la URL del endpoint que creamos en el agregador
        String url = AGREGADOR_API_URL + "/api/colecciones/" + idColeccion + "/fuentes/" + idFuente + "/procesar";

        // Hacemos una llamada POST (sin cuerpo) y no esperamos una respuesta compleja
        restTemplate.postForObject(url, null, String.class);
    }



    public List<SolicitudEliminacionDTO> obtenerSolicitudesDeEliminacion() {
        try {
            // Hacemos la llamada GET al endpoint del agregador
            SolicitudEliminacionDTO[] solicitudes = restTemplate.getForObject(AGREGADOR_API_URL + "/solicitudes-eliminacion", SolicitudEliminacionDTO[].class);

            // Convertimos el array a una lista y la devolvemos
            return solicitudes != null ? Arrays.asList(solicitudes) : Collections.emptyList();
        } catch (Exception e) {
            // Manejo básico de errores: si el agregador no responde, devolvemos una lista vacía
            System.err.println("Error al conectar con el agregador para obtener solicitudes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}




