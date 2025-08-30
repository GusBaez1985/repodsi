package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.models.entities.coleccion.*;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.edu.utn.frba.dds.models.repositories.ISolicitudEliminacionRepository;
import ar.edu.utn.frba.dds.models.services.ServicioGestionSolicitudes;
import ar.edu.utn.frba.dds.models.services.spam.ISpamDetector;
import ar.edu.utn.frba.dds.models.services.spam.SpamDetector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpamTest {

    private ISolicitudEliminacionRepository repository;
    private ISpamDetector spamDetector;
    private ServicioGestionSolicitudes servicio;

    @BeforeEach
    public void setup() {
        repository = mock(ISolicitudEliminacionRepository.class);
        spamDetector = new SpamDetector();
        servicio = new ServicioGestionSolicitudes(spamDetector, repository);
    }

    @Test
    public void solicitudEsSpamYEsRechazada() {
        Hecho hecho = Hecho.of(
                "Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires...",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-36.868375", "-60.343297"),
                LocalDate.of(2001, 11, 29),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );
        SolicitudEliminacion solicitud = servicio.crearYProcesarSolicitud("¡Gana dinero rápido con esta promoción!", hecho, 1L);

        Assertions.assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstadoSolicitud());
    }

    @Test
    public void solicitudNoEsSpamYSinRevisar() {
        Hecho hecho = Hecho.of(
                "Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires...",
                TipoHecho.TEXTO,
                "Caída de aeronave",
                new Ubicacion("-36.868375", "-60.343297"),
                LocalDate.of(2001, 11, 29),
                new FuenteCargaManual("Fuente de Carga Manual para Test")
        );
        SolicitudEliminacion solicitud = servicio.crearYProcesarSolicitud("Considero que este hecho es inexacto", hecho, 2L);

        assertEquals(EstadoSolicitud.SIN_REVISAR, solicitud.getEstadoSolicitud());
    }
}