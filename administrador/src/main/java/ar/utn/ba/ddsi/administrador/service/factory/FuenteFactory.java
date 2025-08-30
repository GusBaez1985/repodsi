package ar.utn.ba.ddsi.administrador.service.factory;

import ar.edu.utn.frba.dds.models.entities.fuente.*;
import ar.edu.utn.frba.dds.models.entities.lectorCSV.LectorDeHechosCSV; // Asegúrate que el import sea correcto
import ar.utn.ba.ddsi.administrador.dto.FuenteDTO;

public class FuenteFactory {
    public static Fuente crear(FuenteDTO dto) {
        // Asumimos que LectorDeHechosCSV es un componente que podemos instanciar aquí.
        // En una aplicación Spring completa, lo inyectaríamos.
        LectorDeHechosCSV lectorCSV = new LectorDeHechosCSV();

        return switch (dto.getTipo().toLowerCase()) {
            case "manual" -> new FuenteCargaManual("Fuente de Carga Manual para Test");
            case "contribuyente" -> new FuenteContribuyente(dto.getEsRegistrado() != null && dto.getEsRegistrado());
            case "dataset" -> {
                if (dto.getPath() == null || dto.getPath().isBlank()) {
                    throw new IllegalArgumentException("El tipo 'dataset' requiere un 'path'.");
                }
                yield new FuenteDataset(lectorCSV, dto.getPath());
            }
            default -> throw new IllegalArgumentException("Tipo de fuente no válido: " + dto.getTipo());
        };
    }
}