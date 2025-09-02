package ar.utn.ba.ddsi.administrador.fuente_dinamica.services;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoEdicionDTO;
import ar.utn.ba.ddsi.administrador.fuente_dinamica.dtos.HechoRevisionDTO;

import java.util.List;

public interface IFuenteDinamicaService {
    //void agregarHecho(Hecho hecho);  // cargado por contribuyente (anónimo o registrado)
    Hecho agregarHecho(Hecho hecho);
    Hecho obtenerHechoPorId(Long id); // para futura edición o validación
    void editarHecho(Long id, HechoEdicionDTO dto);  //
    List<Hecho> obtenerTodosLosHechos(); // para prueba de listar los hechos
    void revisarHecho(Long id, HechoRevisionDTO dto);

}