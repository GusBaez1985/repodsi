/*package ar.utn.ba.ddsi.administrador.fuente_dinamica.services.impl;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoEdicionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.models.repositories.impl.HechoFuenteDinamicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuenteDinamicaServiceImplTest {

	private FuenteDinamicaServiceImpl service;
	private HechoFuenteDinamicaRepository repository;

	@BeforeEach
	void setUp() {
		repository = Mockito.spy(new HechoFuenteDinamicaRepository());
		service = new FuenteDinamicaServiceImpl(repository);
	}

	@Test
	void agregarHechoDeberiaGuardarloEnElRepositorio() {
		Hecho hecho = hechoValido();
		service.agregarHecho(hecho);
		assertTrue(service.obtenerTodosLosHechos().contains(hecho));
	}

	@Test
	void obtenerTodosLosHechosDeberiaDevolverListaCorrecta() {
		Hecho hecho1 = hechoValido();
		Hecho hecho2 = hechoValido();
		service.agregarHecho(hecho1);
		service.agregarHecho(hecho2);

		List<Hecho> hechos = service.obtenerTodosLosHechos();
		assertEquals(2, hechos.size());
	}

	@Test
	void editarHechoDeberiaActualizarDatos() {
		Hecho hecho = hechoConFechaDeCarga(LocalDate.now().minusDays(3));
		service.agregarHecho(hecho);

		HechoEdicionDTO dto = new HechoEdicionDTO();
		dto.setTitulo("Nuevo título");
		dto.setDescripcion("Nueva descripción");
		dto.setCategoria("Cultura");
		dto.setLatitud("-34.7");
		dto.setLongitud("-58.5");
		dto.setFechaAcontecimiento(LocalDate.of(2025, 1, 1));

		service.editarHecho(hecho.getId(), dto);

		Hecho actualizado = service.obtenerHechoPorId(hecho.getId());
		assertEquals("Nuevo título", actualizado.getTitulo());
		assertEquals("Cultura", actualizado.getCategoria());
	}

	@Test
	void editarHechoConPlazoVencidoDeberiaLanzarExcepcion() {
		Hecho hecho = hechoConFechaDeCarga(LocalDate.now().minusDays(10));
		service.agregarHecho(hecho);

		HechoEdicionDTO dto = new HechoEdicionDTO();
		dto.setTitulo("Modificación tardía");

		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			service.editarHecho(hecho.getId(), dto);
		});

		assertTrue(ex.getMessage().contains("Plazo de edición vencido"));
	}

	// 🔧 Helpers

	private Hecho hechoValido() {
		return hechoConFechaDeCarga(LocalDate.now());
	}

	private Hecho hechoConFechaDeCarga(LocalDate fecha) {
		FuenteContribuyente fuenteMock = new FuenteContribuyente();
		fuenteMock.setEsRegistrado(true);

		return Hecho.builder()
				.id(System.nanoTime())
				.titulo("Tormenta")
				.descripcion("Granizo fuerte")
				.tipoHecho(null)
				.categoria("Clima")
				.ubicacion(new Ubicacion("-34.6", "-58.4"))
				.fecAcontecimiento(LocalDate.now())
				.fecCarga(fecha)
				.fuente(fuenteMock)
				.eliminado(false)
				.etiquetas(new ArrayList<>())
				.build();
	}
}
*/