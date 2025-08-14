package ar.utn.ba.ddsi.fuente_dinamica.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.utn.ba.ddsi.fuente_dinamica.dtos.HechoEdicionDTO;
import ar.utn.ba.ddsi.fuente_dinamica.dtos.HechoRevisionDTO;
import ar.utn.ba.ddsi.fuente_dinamica.models.repositories.impl.HechoFuenteDinamicaRepository;
import ar.utn.ba.ddsi.fuente_dinamica.services.IFuenteDinamicaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FuenteDinamicaServiceImpl implements IFuenteDinamicaService {

    private final HechoFuenteDinamicaRepository hechoRepository;

    // Usado por Spring normalmente
    public FuenteDinamicaServiceImpl() {
        this.hechoRepository = new HechoFuenteDinamicaRepository();
    }

    // Usado para test unitarios
    public FuenteDinamicaServiceImpl(HechoFuenteDinamicaRepository repository) {
        this.hechoRepository = repository;
    }

    @Override
    public void agregarHecho(Hecho hecho) {
        hechoRepository.save(hecho);
    }

    @Override
    public Hecho obtenerHechoPorId(Long id) {
        return hechoRepository.findById(id);
    }

    @Override
    public void editarHecho(Long id, HechoEdicionDTO dto) {
        Hecho hechoExistente = hechoRepository.findById(id);
        if (hechoExistente == null) {
            throw new RuntimeException("Hecho no encontrado");
        }

        if (!(hechoExistente.getFuente() instanceof FuenteContribuyente fuente)) {
            throw new RuntimeException("Fuente no válida para edición");
        }

        if (!fuente.isEsRegistrado()) {
            throw new RuntimeException("Solo las fuentes registradas pueden editar hechos");
        }

        if (!hechoExistente.getFecCarga().isAfter(LocalDate.now().minusDays(7))) {
            throw new RuntimeException("Plazo de edición vencido");
        }

        hechoExistente.setTitulo(dto.getTitulo());
        hechoExistente.setDescripcion(dto.getDescripcion());
        hechoExistente.setCategoria(dto.getCategoria());
        hechoExistente.setUbicacion(new Ubicacion(dto.getLatitud(), dto.getLongitud()));
        hechoExistente.setFecAcontecimiento(dto.getFechaAcontecimiento());

        hechoRepository.save(hechoExistente);
    }

    @Override
    public void revisarHecho(Long id, HechoRevisionDTO dto) {
        Hecho hecho = hechoRepository.findById(id);
        if (hecho == null) {
            throw new RuntimeException("Hecho no encontrado");
        }

        hecho.setEstadoRevision(dto.getEstado());
        hechoRepository.save(hecho);
    }

    @Override
    public List<Hecho> obtenerTodosLosHechos() {
        return hechoRepository.findAll();
    }
}
