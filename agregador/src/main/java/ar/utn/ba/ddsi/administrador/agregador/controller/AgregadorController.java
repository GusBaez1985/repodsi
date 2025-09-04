package ar.utn.ba.ddsi.administrador.agregador.controller;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.administrador.dto.HechoResponseDTO;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.administrador.agregador.dto.ColeccionRequestDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO;
import ar.utn.ba.ddsi.administrador.service.factory.AlgoritmoDeConsensoFactory;
import ar.utn.ba.ddsi.administrador.service.factory.FuenteFactory;
import ar.utn.ba.ddsi.administrador.agregador.dto.ColeccionResponseDTO;
import ar.utn.ba.ddsi.administrador.agregador.dto.FuenteResponseDTO;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgregadorController {

    private final IColeccionRepository coleccionRepository;
    private final IFuenteRepository fuenteRepository;

    public AgregadorController(IColeccionRepository coleccionRepository, IFuenteRepository fuenteRepository) {
        this.coleccionRepository = coleccionRepository;
        this.fuenteRepository = fuenteRepository;
    }

    @GetMapping("/colecciones")
    public List<ColeccionResponseDTO> listarColecciones() {
        List<Coleccion> colecciones = coleccionRepository.findAll();

        // Mapeamos cada entidad Coleccion a su DTO de respuesta
        return colecciones.stream().map(coleccion -> {

            // Mapeamos cada entidad Fuente a su DTO de respuesta
            List<FuenteResponseDTO> fuentesDTO = coleccion.getFuentes().stream().map(fuente -> {
                String tipo = "desconocido";
                String nombre = "Sin Nombre"; // Valor por defecto
                if (fuente instanceof FuenteContribuyente) {
                    tipo = "contribuyente";
                    nombre = "Aportes de Contribuyentes"; // Ejemplo
                } else if (fuente instanceof FuenteCargaManual) {
                    tipo = "manual";
                    nombre = "Carga Manual"; // Ejemplo
                } else if (fuente instanceof FuenteDataset) {
                    tipo = "dataset";
                    nombre = "Dataset Externo"; // Ejemplo
                }
                return new FuenteResponseDTO(fuente.getId(), tipo, nombre);
            }).collect(Collectors.toList());

            String tipoAlgoritmo = coleccion.getAlgoritmoDeConsenso() != null ?
                    coleccion.getAlgoritmoDeConsenso().getClass().getSimpleName() : null;

            return new ColeccionResponseDTO(
                    coleccion.getId(),
                    coleccion.getTitulo(),
                    coleccion.getDescripcion(),
                    tipoAlgoritmo,
                    fuentesDTO
            );
        }).collect(Collectors.toList());
    }

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    @PostMapping("/colecciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Coleccion crearColeccion(@RequestBody ColeccionRequestDTO dto) {
        // 1. Se crea la colección. El constructor no maneja el algoritmo, lo deja en null.
        //    El CriterioDePertenencia tampoco se define en este punto del flujo, por lo que se pasa null.
        Coleccion nuevaColeccion = new Coleccion(dto.getTitulo(), dto.getDescripcion(), null);

        // 2. Se crea la instancia del algoritmo a partir del string del DTO.
        AlgoritmoDeConsenso algoritmo = AlgoritmoDeConsensoFactory.crear(dto.getTipoAlgoritmo());

        // 3. Se asigna el algoritmo a la colección recién creada usando su setter.
        nuevaColeccion.setAlgoritmoDeConsenso(algoritmo);

        coleccionRepository.save(nuevaColeccion);
        return nuevaColeccion;
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Coleccion> obtenerColeccion(@PathVariable Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(coleccion);
    }



    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> agregarFuenteAColeccion(@PathVariable("id") Long id, @RequestBody FuenteDTO dto) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            System.out.println("[AGREGADOR DEBUG] Colección no encontrada con ID: " + id);
            return ResponseEntity.notFound().build();
        }
        Fuente nuevaFuente = FuenteFactory.crear(dto);

        // Primero guardamos la fuente para que obtenga su ID único
        fuenteRepository.save(nuevaFuente);

        coleccion.agregarFuente(nuevaFuente);
        coleccionRepository.save(coleccion);

        System.out.println("[AGREGADOR DEBUG] Fuente '" + dto.getNombre() + "' con ID " + nuevaFuente.getId() + " fue agregada a Colección ID " + id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/colecciones/{id}/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechosDeColeccion(@PathVariable("id") Long id) {
        Coleccion coleccion = coleccionRepository.findById(id);
        if (coleccion == null) {
            return ResponseEntity.notFound().build();
        }
        // Devuelve la lista de entidades de dominio 'Hecho' completas
        return ResponseEntity.ok(coleccion.getHechos());
    }

}