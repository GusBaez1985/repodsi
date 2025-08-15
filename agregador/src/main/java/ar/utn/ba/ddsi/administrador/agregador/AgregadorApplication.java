package ar.utn.ba.ddsi.administrador.agregador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
		"ar.utn.ba.ddsi.agregador",
		"ar.utn.ba.ddsi.consenso",
		"ar.edu.utn.frba.dds.models.repositories"
})
@EnableScheduling
public class AgregadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorApplication.class, args);
	}
}
