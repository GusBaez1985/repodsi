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
    private final String urlAgregador = "http://localhost:8080/api";
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
        String url = AGREGADOR_API_URL + "/api/colecciones";
        return restTemplate.postForObject(url, dto, ColeccionResponseDTO.class);
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

        // 1. Recibimos la respuesta del agregador en un DTO genérico (Object)
        //    para evitar problemas de deserialización directa.
        Object[] respuesta = restTemplate.getForObject(url, Object[].class);

        if (respuesta == null) {
            return Collections.emptyList();
        }

        // 2. Mapeamos manualmente el resultado.
        //    Esto nos da control total sobre la conversión y evita errores.
        return Arrays.stream(respuesta)
                .map(objetoHecho -> {
                    // Convertimos el Object genérico a un mapa de clave-valor
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> mapaHecho = (java.util.Map<String, Object>) objetoHecho;

                    // Extraemos solo el campo que nos interesa: "descripcion"
                    String descripcion = (String) mapaHecho.get("descripcion");

                    // Creamos el DTO que el administrador realmente necesita
                    return new HechoResponseDTO(descripcion);
                })
                .collect(Collectors.toList());
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
            SolicitudEliminacionDTO[] solicitudes = restTemplate.getForObject(urlAgregador + "/solicitudes-eliminacion", SolicitudEliminacionDTO[].class);

            // Convertimos el array a una lista y la devolvemos
            return solicitudes != null ? Arrays.asList(solicitudes) : Collections.emptyList();
        } catch (Exception e) {
            // Manejo básico de errores: si el agregador no responde, devolvemos una lista vacía
            System.err.println("Error al conectar con el agregador para obtener solicitudes: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
