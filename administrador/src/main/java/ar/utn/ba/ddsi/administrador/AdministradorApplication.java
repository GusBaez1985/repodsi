package ar.utn.ba.ddsi.administrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Con solo @SpringBootApplication, Spring escaneará únicamente los paquetes
// dentro de "ar.utn.ba.ddsi.administrador", que es el comportamiento correcto.
@SpringBootApplication
public class AdministradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministradorApplication.class, args);
    }
}