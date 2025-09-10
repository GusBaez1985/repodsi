package ar.utn.ba.ddsi.servicio_estadisticas.models.dtos.input;

import lombok.Getter;

//@Getter
public class NominatimResponse {
    private Address address;

    public Address getAddress() {
        return address;
    }

    public static class Address {
        private String state;

        public String getState() {
            return state;
        }
    }
}
