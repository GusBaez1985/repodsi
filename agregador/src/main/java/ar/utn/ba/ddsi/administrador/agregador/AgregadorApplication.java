package ar.utn.ba.ddsi.administrador.agregador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // ¡Sin argumentos!
@EnableScheduling
@EntityScan(basePackages = {"ar.edu.utn.frba.dds.models"})
public class AgregadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgregadorApplication.class, args);
    }
}