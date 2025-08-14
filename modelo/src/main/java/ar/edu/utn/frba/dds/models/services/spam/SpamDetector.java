package ar.edu.utn.frba.dds.models.services.spam;

import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SpamDetector implements ISpamDetector {

    private static final List<String> PALABRAS_CLAVE_SPAM = Arrays.asList(
            "oferta", "gratis", "promoción", "descuento", "gana dinero", "ganar dinero", "cialis",
            "casino", "apuestas", "urgente!!!", "urgente", "haz clic aquí", "click aquí", "compra ahora",
            "ingresos extra", "oportunidad única", "última oportunidad", "bitcoin", "crypto"
    );

    @Override
    public boolean esSpam(SolicitudEliminacion solicitud) {
        if (solicitud == null || solicitud.getMotivo() == null || solicitud.getMotivo().trim().isEmpty()) {
            return false;
        }

        String motivoEnMinusculas = solicitud.getMotivo().toLowerCase(Locale.ROOT);

        return PALABRAS_CLAVE_SPAM.stream()
                .anyMatch(palabra -> motivoEnMinusculas.contains(palabra.toLowerCase(Locale.ROOT)));
    }
}