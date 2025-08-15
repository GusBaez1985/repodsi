package ar.utn.ba.ddsi.administrador;

import ar.utn.ba.ddsi.administrador.agregador.models.repositories.IColeccionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ar.utn.ba.ddsi.administrador",
        "ar.utn.ba.ddsi.administrador.agregador"
})
public class AdministradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministradorApplication.class, args);
    }
}