package ar.edu.utn.frba.dds.domain.coleccion;

import ar.edu.utn.frba.dds.domain.criterios.CriterioDePertenencia;
import ar.edu.utn.frba.dds.domain.criterios.CriterioFechaEntre;
import ar.edu.utn.frba.dds.domain.criterios.CriterioPorCategoria;
import ar.edu.utn.frba.dds.domain.fuente.FuenteCargaManual;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ColeccionTest {

    //  Método auxiliar que crea y devuelve todos los hechos del escenario 1
    private List<Hecho> hechosDePrueba() {
        Hecho hecho1 = Hecho.of(
                "Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-36.868375", "-60.343297"),
                LocalDate.of(2001, 11, 29),
                new FuenteCargaManual()

        );

        Hecho hecho2 = Hecho.of(
                "Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén",
                "Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                TipoHecho.TEXTO,
                "Accidente con maquinaria industrial",
                new Ubicacion("-37.345571", "-70.241485"),
                LocalDate.of(2001, 8, 16),
                new FuenteCargaManual()
        );

        Hecho hecho3 = Hecho.of(
                "Caída de aeronave impacta en Venado Tuerto, Santa Fe",
                "Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-33.768051", "-61.921032"),
                LocalDate.of(2008, 8, 8),
                new FuenteCargaManual()
        );

        Hecho hecho4 = Hecho.of(
                "Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires",
                "Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de DesarrolloSocial está brindando apoyo a los damnificados.",
                TipoHecho.TEXTO,
                "Accidente en paso a nivel",
                new Ubicacion("-35.855811", "-61.940589"),
                LocalDate.of(2020, 1, 27),
                new FuenteCargaManual()
        );

        Hecho hecho5 = Hecho.of(
                "Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña",
                "Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
                TipoHecho.TEXTO,
                "Derrumbe en obra en construcción",
                new Ubicacion("-26.780008", "-60.458782"),
                LocalDate.of(2016, 6, 4),
                new FuenteCargaManual()
        );

        return List.of(hecho1, hecho2, hecho3, hecho4, hecho5);
    }

    @Test
    public void crearColeccionConHechos() {
        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", null);
        coleccion.getHechos().addAll(hechosDePrueba());

        assertEquals(5, coleccion.getHechos().size());
        assertEquals("Colección prueba", coleccion.getTitulo());
    }

    // test 1.2.1
    @Test
    public void aplicarCriterioFecha() {
        List<Hecho> hechos = hechosDePrueba();
        CriterioDePertenencia criterio = new CriterioFechaEntre(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1)
        );

        Coleccion coleccion = new Coleccion("Con criterio fecha", "Filtro por fecha", criterio);
        coleccion.getHechos().addAll(hechos);

        List<Hecho> filtrados = coleccion.getHechos().stream()
                .filter(h -> coleccion.getCriterio().cumpleCriterio(h))
                .toList();

        assertEquals(3, filtrados.size());
    }

    // test 1.2.2
    @Test
    public void aplicarCriterioCategoria() {
        List<Hecho> hechos = hechosDePrueba();

        // Primero filtro por fecha como en el test anterior
        CriterioDePertenencia criterioFecha = new CriterioFechaEntre(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1)
        );
        List<Hecho> filtradosPorFecha = hechos.stream()
                .filter(h -> criterioFecha.cumpleCriterio(h))
                .toList();

        // Ahora filtro por categoría
        CriterioDePertenencia criterioCategoria = new CriterioPorCategoria("Caída de aeronave");
        List<Hecho> filtradosPorCategoria = filtradosPorFecha.stream()
                .filter(h -> criterioCategoria.cumpleCriterio(h))
                .toList();

        assertEquals(2, filtradosPorCategoria.size());
    }

    // test 1.3
    @Test
    public void filtroVisualizador() {
        Coleccion coleccion = new Coleccion("Colección visualizador", "Test filtro visual", null);
        coleccion.getHechos().addAll(hechosDePrueba());

        String categoriaFiltro = "Caída de aeronave";
        String tituloFiltro = "un título"; // No coincide

        List<Hecho> filtrados = coleccion.getHechos().stream()
                .filter(h -> h.getCategoria().equalsIgnoreCase(categoriaFiltro))
                .filter(h -> h.getTitulo().equalsIgnoreCase(tituloFiltro))
                .toList();

        assertEquals(0, filtrados.size());
    }

    @Test
    public void hechosConEtiquetas() {
        List<Hecho> hechos = hechosDePrueba();

        // Busco el hecho en cuestion
        Hecho hecho = hechos.stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase("Caída de aeronave impacta en Olavarría"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));

        // Agrego las etiquetas
        hecho.agregarEtiqueta("Olavarría");
        hecho.agregarEtiqueta("Grave");

        // Verifico que las etiquetas estén presentes
        assertEquals(2, hecho.getEtiquetas().size());
        assertTrue(hecho.getEtiquetas().contains("Olavarría"));
        assertTrue(hecho.getEtiquetas().contains("Grave"));
    }



}
