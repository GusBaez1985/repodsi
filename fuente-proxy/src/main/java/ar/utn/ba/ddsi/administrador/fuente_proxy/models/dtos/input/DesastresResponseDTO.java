package ar.utn.ba.ddsi.administrador.fuente_proxy.models.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class DesastresResponseDTO {
    private List<DesastreDTO> data;
}
