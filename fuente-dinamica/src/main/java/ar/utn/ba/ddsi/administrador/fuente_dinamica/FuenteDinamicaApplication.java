package ar.utn.ba.ddsi.administrador.fuente_dinamica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"ar.edu.utn.frba.dds.models"})
public class FuenteDinamicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuenteDinamicaApplication.class, args);
	}

}
