package ar.utn.ba.ddsi.administrador.service;

import algoritmosDeConsenso.ConsensoAbsoluto;
import algoritmosDeConsenso.ConsensoMayoriaSimple;
import algoritmosDeConsenso.ConsensoMultiplesMenciones;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
//import ar.edu.utn.frba.dds.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.administrador.dto.ColeccionDTO;
import ar.utn.ba.ddsi.administrador.dto.ColeccionResponseDTO;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final IColeccionRepository coleccionRepository;

    public AdministradorService(IColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
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

        if (dto.getTipoAlgoritmo() != null) {
            switch (dto.getTipoAlgoritmo().toLowerCase()) {
                case "absoluto":
                    coleccion.setAlgoritmoDeConsenso(new ConsensoAbsoluto());
                    break;
                case "mayoria_simple":
                    coleccion.setAlgoritmoDeConsenso(new ConsensoMayoriaSimple());
                    break;
                case "multiples_menciones":
                    coleccion.setAlgoritmoDeConsenso(new ConsensoMultiplesMenciones());
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de algoritmo inválido");
            }
        }


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

        AlgoritmoDeConsenso algoritmo = switch (tipoAlgoritmo.toLowerCase()) {
            case "absoluto" -> new ConsensoAbsoluto();
            case "mayoria_simple" -> new ConsensoMayoriaSimple();
            case "multiples_menciones" -> new ConsensoMultiplesMenciones();
            default -> throw new IllegalArgumentException("Tipo de algoritmo inválido");
        };

        coleccion.setAlgoritmoDeConsenso(algoritmo);
        coleccionRepository.save(coleccion);
    }


}
