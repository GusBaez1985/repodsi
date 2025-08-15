package ar.utn.ba.ddsi.administrador.agregador.config;

import ar.edu.utn.frba.dds.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.models.repositories.impl.HechoFuenteDinamicaRepository;
import ar.utn.ba.ddsi.administrador.fuente_estatica.models.repositories.impl.HechoFuenteEstaticaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean(name = "fuenteDinamica")
    public IHechoRepository fuenteDinamica() {
        return new HechoFuenteDinamicaRepository();
    }

    @Bean(name = "fuenteEstatica")
    public IHechoRepository fuenteEstatica() {
        return new HechoFuenteEstaticaRepository();
    }

    /*
    @Bean
    public IHechoRepository fuenteDinamica() {
        return new HechoFuenteDinamicaRepository();
    }

    @Bean
    public IHechoRepository fuenteEstatica() {
        return new HechoFuenteEstaticaRepository();
    }*/
}