package ar.utn.ba.ddsi.agregador.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.EstadoSolicitud;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.services.spam.ISpamDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AgregadorServiceTest {

	private AgregadorService agregadorService;

	@BeforeEach
	void setUp() {
		Fuente fuenteMock = new Fuente() {
			private List<Hecho> hechos = List.of(
					Hecho.of("Incendio en el bosque", "Fuerte incendio en zona norte", TipoHecho.TEXTO, "Incendio",
							new Ubicacion("-34.6037", "-58.3816"), LocalDate.now(), this),
					Hecho.of("Inundación en la ciudad", "Inundación en la capital", TipoHecho.TEXTO, "Inundación",
							new Ubicacion("-34.6158", "-58.4333"), LocalDate.now(), this)
			);

			@Override public void importarHechos() { /* Simula traer datos */ }
			@Override public List<Hecho> getHechos() { return hechos; }
			@Override public void agregarHecho(Hecho hecho) { }
			@Override public void eliminarHecho(Hecho hecho) { }
		};

		ISpamDetector spamDetector = solicitud -> solicitud.getMotivo().toLowerCase().contains("spam");

		agregadorService = new AgregadorService(List.of(fuenteMock), spamDetector);
	}

	@Test
	void testObtenerTodosLosHechos() {
		List<Hecho> hechos = agregadorService.obtenerTodosLosHechos();
		assertEquals(2, hechos.size());
		assertTrue(hechos.stream().anyMatch(h -> h.getTitulo().equals("Incendio en el bosque")));
		assertTrue(hechos.stream().anyMatch(h -> h.getTitulo().equals("Inundación en la ciudad")));
	}

	@Test
	void testObtenerHechosPorCategoria() {
		List<Hecho> incendios = agregadorService.obtenerHechosPorCategoria("Incendio");
		assertEquals(1, incendios.size());
		assertEquals("Incendio", incendios.get(0).getCategoria());
		List<Hecho> terremotos = agregadorService.obtenerHechosPorCategoria("Terremoto");
		assertTrue(terremotos.isEmpty());
	}

	@Test
	void testProcesarSolicitudEliminacionSpam() {
		Hecho hecho = Hecho.of("Prueba", "Hecho de prueba", TipoHecho.TEXTO, "Prueba",
				new Ubicacion("0", "0"), LocalDate.now(), null);
		SolicitudEliminacion solicitud = new SolicitudEliminacion("Este es un mensaje spam", hecho, 1L);

		agregadorService.procesarSolicitudEliminacion(solicitud);

		assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoSolicitud());
		assertFalse(solicitud.getHecho().getEliminado());
	}

	@Test
	void testProcesarSolicitudEliminacionValida() {
		Hecho hecho = Hecho.of("Prueba", "Hecho de prueba", TipoHecho.TEXTO, "Prueba",
				new Ubicacion("0", "0"), LocalDate.now(), null);
		SolicitudEliminacion solicitud = new SolicitudEliminacion("Este es un motivo válido", hecho, 2L);

		agregadorService.procesarSolicitudEliminacion(solicitud);

		assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstadoSolicitud());
		assertTrue(solicitud.getHecho().getEliminado());
	}

	@Test
	void testFlujoCompletoConSpam() {
		List<Hecho> hechos = agregadorService.obtenerTodosLosHechos();
		assertEquals(2, hechos.size(), "Debería haber 2 hechos de la fuente mock.");

		Hecho hechoSpam = hechos.get(0);
		SolicitudEliminacion solicitudSpam = new SolicitudEliminacion("Gana dinero rápido con esta promo spam", hechoSpam, 1L);
		agregadorService.procesarSolicitudEliminacion(solicitudSpam);

		assertEquals(EstadoSolicitud.RECHAZADA, solicitudSpam.getEstadoSolicitud());
		assertFalse(solicitudSpam.getHecho().getEliminado());

		Hecho hechoValido = hechos.get(1);
		SolicitudEliminacion solicitudValida = new SolicitudEliminacion("Este hecho tiene datos incorrectos, por favor eliminar.", hechoValido, 2L);
		agregadorService.procesarSolicitudEliminacion(solicitudValida);

		assertEquals(EstadoSolicitud.APROBADA, solicitudValida.getEstadoSolicitud());
		assertTrue(solicitudValida.getHecho().getEliminado());
	}
}