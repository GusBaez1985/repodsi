package ar.utn.ba.ddsi.administrador.service;

import algoritmosDeConsenso.ConsensoAbsoluto;
import algoritmosDeConsenso.ConsensoMayoriaSimple;
import algoritmosDeConsenso.ConsensoMultiplesMenciones;
import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;
import ar.utn.ba.ddsi.administrador.agregador.models.repositories.impl.FuenteRepository;
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
