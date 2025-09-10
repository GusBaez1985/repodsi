package ar.utn.ba.ddsi.administrador.fuente_dinamica.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoEdicionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoRevisionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.services.IFuenteDinamicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FuenteDinamicaServiceImpl implements IFuenteDinamicaService {

    private final IHechoRepository hechoRepository;
    private final IFuenteRepository fuenteRepository;

    public FuenteDinamicaServiceImpl(IHechoRepository hechoRepository, IFuenteRepository fuenteRepository) {
        this.hechoRepository = hechoRepository;
        this.fuenteRepository = fuenteRepository;
    }

    @Override
    public Hecho agregarHecho(Hecho hecho) {
        FuenteContribuyente fuente = obtenerFuenteDeContribuyentes();
        hecho.setFuente(fuente); // <-- Esto ahora compila gracias al cambio en Hecho.java
        return hechoRepository.save(hecho);
    }

    @Override
    public Hecho obtenerHechoPorId(Long id) {
        return hechoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));
    }

    @Override
    public List<Hecho> obtenerTodosLosHechos() {
        return hechoRepository.findAll();
    }

    @Override
    public void editarHecho(Long id, HechoEdicionDTO dto) {
        Hecho hechoExistente = obtenerHechoPorId(id);

        if (!(hechoExistente.getFuente() instanceof FuenteContribuyente fuente)) {
            throw new RuntimeException("Fuente no válida para edición");
        }
        if (!fuente.isEsRegistrado()) {
            throw new RuntimeException("Solo las fuentes registradas pueden editar hechos");
        }
        if (hechoExistente.getFecCarga() == null || !hechoExistente.getFecCarga().isAfter(LocalDate.now().minusDays(7).atStartOfDay())) {
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
        Hecho hecho = obtenerHechoPorId(id);
        hecho.setEstadoRevision(dto.getEstado());
        hechoRepository.save(hecho);
    }

    private FuenteContribuyente obtenerFuenteDeContribuyentes() {
        Optional<FuenteContribuyente> fuenteOpt = fuenteRepository.findAll()
                .stream()
                .filter(f -> f instanceof FuenteContribuyente)
                .map(f -> (FuenteContribuyente) f)
                .findFirst();

        if (fuenteOpt.isPresent()) {
            return fuenteOpt.get();
        } else {
            FuenteContribuyente nuevaFuente = new FuenteContribuyente();
            // Ya no intentamos llamar a setNombre, porque no existe
            return fuenteRepository.save(nuevaFuente);
        }
    }
}