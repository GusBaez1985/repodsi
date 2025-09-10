/*package ar.edu.utn.frba.dds.models.contribuyente;
import ar.edu.utn.frba.dds.models.entities.coleccion.*;
import ar.edu.utn.frba.dds.models.entities.contribuyente.Contribuyente;
import ar.edu.utn.frba.dds.models.entities.criterios.CriterioFechaEntre;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContribuyenteTest {

    private List<Hecho> hechosDePrueba() {

        Hecho hecho1 = Hecho.of(
                "Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires...",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-36.868375", "-60.343297"),
                LocalDate.of(2001, 11, 29),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );

        Hecho hecho2 = Hecho.of(
                "Serio incidente: Accidente con maquinaria industrial en Chos Malal",
                "Un grave accidente con maquinaria industrial se registró en Chos Malal...",
                TipoHecho.TEXTO,
                "Accidente con maquinaria industrial",
                new Ubicacion("-37.345571", "-70.241485"),
                LocalDate.of(2001, 8, 16),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );

        Hecho hecho3 = Hecho.of(
                "Caída de aeronave impacta en Venado Tuerto, Santa Fe",
                "Grave caída de aeronave ocurrió en Venado Tuerto, Santa Fe...",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-33.768051", "-61.921032"),
                LocalDate.of(2008, 8, 8),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );

        Hecho hecho4 = Hecho.of(
                "Accidente en paso a nivel deja múltiples daños en Pehuajó",
                "Grave accidente en paso a nivel en Pehuajó, Buenos Aires...",
                TipoHecho.TEXTO,
                "Accidente en paso a nivel",
                new Ubicacion("-35.855811", "-61.940589"),
                LocalDate.of(2020, 1, 27),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );

        Hecho hecho5 = Hecho.of(
                "Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
                "Un grave derrumbe en obra se registró en Chaco...",
                TipoHecho.TEXTO,
                "Derrumbe en obra en construcción",
                new Ubicacion("-26.780008", "-60.458782"),
                LocalDate.of(2016, 6, 4),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );

        return List.of(hecho1, hecho2, hecho3, hecho4, hecho5);
    }

    @Test
    public void contribuyenteSinFiltro() {
        Contribuyente contribuyente1 = new Contribuyente();

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", null);
        coleccion.getHechos().addAll(hechosDePrueba());

        List<Hecho> resultado = contribuyente1.obtenerHechos(coleccion, List.of());

        Assertions.assertEquals(5, resultado.size());
    }

    @Test
    public void contribuyenteConFiltroDeFecha() {

        Contribuyente contribuyente2 = new Contribuyente();

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", null);
        coleccion.getHechos().addAll(hechosDePrueba());

        CriterioFechaEntre criterioFechaEntre = new CriterioFechaEntre((LocalDate.of(2000, 8, 16)),(LocalDate.of(2002, 8, 16)));

        List<Hecho> resultado = contribuyente2.obtenerHechos(coleccion, List.of(criterioFechaEntre));

        Assertions.assertEquals(2, resultado.size());
    }

@Test
    public void contribuyentePuedeRealizarUnaSolicitudDeEliminacion() {
        Contribuyente contribuyente3 = new Contribuyente("Lucas", "Pérez", 30);

                String motivo = "Este hecho contiene datos incorrectos ".repeat(20); // >500 caracteres

        
        List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList<>();
        solicitudesEliminacion.add(contribuyente3.realizarSolicitudEliminacion(hechosDePrueba().get(1), motivo));

        assertEquals(1,solicitudesEliminacion.size());
    }

}*/