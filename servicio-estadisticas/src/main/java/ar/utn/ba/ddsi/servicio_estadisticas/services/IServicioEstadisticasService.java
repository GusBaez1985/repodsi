package ar.utn.ba.ddsi.servicio_estadisticas.services;

import ar.edu.utn.frba.dds.models.entities.coleccion.Coleccion;

public interface IServicioEstadisticasService {
    public String obtenerProvinciaConMasReportes(Long idColeccion);
    public String obtenerCategoriaConMasReportes();
    public String obtenerProvinciaConMasReportesSegunCategoria(String categoria);
    public Integer obtenerHoraDondeOcurrenMasReportesSegunCategoria(String categoria);
    public Integer obtenerCantidadDeSolicitudesSpam();
    public String exportarEstadisticasCSV(String ruta);
}
