package ar.utn.ba.ddsi.administrador.service.factory; // O donde prefieras ubicarla

import algoritmosDeConsenso.*;
import ar.edu.utn.frba.dds.models.entities.interfaces.AlgoritmoDeConsenso;

public class AlgoritmoDeConsensoFactory {
    public static AlgoritmoDeConsenso crear(String tipoAlgoritmo) {
        if (tipoAlgoritmo == null || tipoAlgoritmo.isBlank()) {
            return null; // No se especifica algoritmo
        }
        return switch (tipoAlgoritmo.toLowerCase()) {
            case "absoluto" -> new ConsensoAbsoluto();
            case "mayoria_simple" -> new ConsensoMayoriaSimple();
            case "multiples_menciones" -> new ConsensoMultiplesMenciones();
            default -> throw new IllegalArgumentException("Tipo de algoritmo inválido: " + tipoAlgoritmo);
        };
    }
}