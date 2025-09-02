

package ar.utn.ba.ddsi.api_metamapa.services.impl;
import ar.utn.ba.ddsi.api_metamapa.services.ISeederService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SeederService implements ISeederService {

    // El constructor ahora está vacío, ya no pide repositorios.
    public SeederService() {
    }

    @Override
    public void init() {
        System.out.println("✅ Seeder ejecutado (sin carga de datos)");
    }

    @PostConstruct
    public void runSeeder() {
        this.init();
    }
}


/* // COMENTAMOS TODO EL CÓDIGO ANTIGUO
package ar.utn.ba.ddsi.api_metamapa.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioPorCategoria;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.edu.utn.frba.dds.models.repositories.IColeccionRepository;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.api_metamapa.services.ISeederService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SeederService implements ISeederService {
    private final IColeccionRepository coleccionRepository;
    private final IHechoRepository hechoRepository;

    public SeederService(IColeccionRepository coleccionRepository, IHechoRepository hechoRepository) {
        this.coleccionRepository = coleccionRepository;
        this.hechoRepository = hechoRepository;
    }

    @Override
    public void init() {

        // Fuente y ubicación ficticia
        Fuente fuenteDummy = new FuenteCargaManual("Fuente");
        Ubicacion ubicacionDummy = new Ubicacion("40.7128", "-74.0060"); // Latitud y longitud de ejemplo

        // Hecho 1
        Hecho hecho1 = Hecho.of(
                "Revolución de Mayo",
                "Primer gobierno patrio en el Virreinato del Río de la Plata.",
                TipoHecho.TEXTO,
                "Historia",
                ubicacionDummy,
                LocalDate.of(1810, 5, 25),
                fuenteDummy
        );
        hecho1.agregarEtiqueta("Independencia");

        // Hecho 2
        Hecho hecho2 = Hecho.of(
                "Descubrimiento de la Penicilina",
                "Alexander Fleming descubre la penicilina.",
                TipoHecho.TEXTO,
                "Ciencia",
                ubicacionDummy,
                LocalDate.of(1928, 9, 28),
                fuenteDummy
        );
        hecho2.agregarEtiqueta("Salud");

        // Colección 1
        Coleccion coleccion1 = new Coleccion(
                "Hechos Históricos Relevantes",
                "Una colección de eventos clave en la historia.",
                new CriterioPorCategoria("Historia")
        );
        coleccion1.getHechos().add(hecho1);
        coleccion1.getHechosConsensuados().add(hecho1);

        // Colección 2
        Coleccion coleccion2 = new Coleccion(
                "Avances Científicos",
                "Inventos y descubrimientos que cambiaron el mundo.",
                new CriterioPorCategoria("Ciencia")
        );
        coleccion2.getHechos().add(hecho2);
        coleccion2.getHechosConsensuados().add(hecho2);

        // Guardar
        coleccionRepository.save(coleccion1);
        coleccionRepository.save(coleccion2);

        hechoRepository.save(hecho1);
        hechoRepository.save(hecho2);

        System.out.println("✅ Seeder ejecutado");
    }

    @PostConstruct
    public void runSeeder() {
        this.init();
    }
}
*/