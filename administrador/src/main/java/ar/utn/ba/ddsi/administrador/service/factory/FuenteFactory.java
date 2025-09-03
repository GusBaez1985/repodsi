package ar.utn.ba.ddsi.administrador.service.factory;

import ar.edu.utn.frba.dds.models.entities.fuente.Fuente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteCargaManual;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteContribuyente;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO; // <-- DTO que llega desde el administrador

public class FuenteFactory {
    /**
     * Crea una instancia de una entidad Fuente a partir de un DTO.
     * Esta versión está simplificada para el contexto del Agregador.
     * No necesita el lector de CSV, solo instancia las entidades.
     */
    public static Fuente crear(FuenteDTO dto) {
        return switch (dto.getTipo().toLowerCase()) {
            case "manual" -> new FuenteCargaManual(dto.getPath());
            case "contribuyente" -> new FuenteContribuyente(dto.getEsRegistrado() != null && dto.getEsRegistrado());
            case "dataset" -> {
                if (dto.getPath() == null || dto.getPath().isBlank()) {
                    throw new IllegalArgumentException("El tipo 'dataset' requiere un 'path'.");
                }
                // A diferencia del administrador, aquí no necesitamos el LectorCSV,
                // solo el path que se usará en otra parte.
                yield new FuenteDataset(null, dto.getPath());
            }
            default -> throw new IllegalArgumentException("Tipo de fuente no válido: " + dto.getTipo());
        };
    }
}