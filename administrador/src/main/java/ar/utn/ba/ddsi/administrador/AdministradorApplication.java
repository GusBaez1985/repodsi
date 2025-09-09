package ar.utn.ba.ddsi.administrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// Con solo @SpringBootApplication, Spring escaneará únicamente los paquetes
// dentro de "ar.utn.ba.ddsi.administrador", que es el comportamiento correcto.
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

public class AdministradorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdministradorApplication.class, args);
    }
}