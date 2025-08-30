package ar.utn.ba.ddsi.administrador.dto;

import lombok.Getter;

@Getter
public class FuenteDTO {
    /**
     * Un nombre descriptivo para la fuente. Ej: "Carga manual de voluntarios".
     */
    private String nombre;

    /**
     * El tipo de fuente a crear. Es el campo más importante.
     */
    private String tipo;

    /**
     * La ruta al archivo CSV. Solo se usa si el tipo es "dataset".
     * Ej: "data/incendios_2025.csv"
     */
    private String path;

    /**
     * Indica si la fuente de contribuyentes es para usuarios registrados.
     * Solo se usa si el tipo es "contribuyente".
     */
    private Boolean esRegistrado;
}