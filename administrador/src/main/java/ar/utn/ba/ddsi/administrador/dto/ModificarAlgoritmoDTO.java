// Archivo: ModificarAlgoritmoDTO.java
package ar.utn.ba.ddsi.administrador.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // Lombok genera un constructor sin argumentos
public class ModificarAlgoritmoDTO {
    private String tipo;

    // Constructor para que el service pueda crearlo fácilmente
    public ModificarAlgoritmoDTO(String tipo) {
        this.tipo = tipo;
    }
}