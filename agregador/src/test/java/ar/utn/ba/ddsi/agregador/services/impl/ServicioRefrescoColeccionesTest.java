package ar.utn.ba.ddsi.agregador.services.impl;

import static org.mockito.Mockito.*;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ServicioRefrescoColeccionesTest {

	private IHechoRepository fuenteDinamica;
	private IHechoRepository fuenteEstatica;
	private ServicioRefrescoColecciones servicio;

	@BeforeEach
	void setUp() {
		// Crear los mocks
		fuenteDinamica = mock(IHechoRepository.class);
		fuenteEstatica = mock(IHechoRepository.class);

		// Inyectar los mocks en el servicio
		servicio = new ServicioRefrescoColecciones(fuenteDinamica, fuenteEstatica);
	}

	@Test
	void testRefrescarColeccionesLlamaSaveEnCadaFuente() {
		// Crear mocks de hechos
		Hecho hecho1 = mock(Hecho.class);
		Hecho hecho2 = mock(Hecho.class);

		// Configurar los repositorios para devolver los hechos
		when(fuenteDinamica.findAll()).thenReturn(List.of(hecho1));
		when(fuenteEstatica.findAll()).thenReturn(List.of(hecho2));

		// Ejecutar el refresco
		servicio.refrescarColecciones();

		// Verificar que se haya llamado save() una vez por cada hecho
		verify(fuenteDinamica, times(1)).save(hecho1);
		verify(fuenteEstatica, times(1)).save(hecho2);

		// Verificar que se haya llamado findAll() en cada fuente
		verify(fuenteDinamica, times(1)).findAll();
		verify(fuenteEstatica, times(1)).findAll();
	}
}
