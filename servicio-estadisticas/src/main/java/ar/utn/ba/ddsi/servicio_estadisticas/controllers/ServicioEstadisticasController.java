package ar.utn.ba.ddsi.servicio_estadisticas.controllers;

import ar.utn.ba.ddsi.servicio_estadisticas.services.IServicioEstadisticasService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadisticas")
public class ServicioEstadisticasController {

    private final IServicioEstadisticasService servicioEstadisticas;

    public ServicioEstadisticasController(IServicioEstadisticasService servicioEstadisticas) {
        this.servicioEstadisticas = servicioEstadisticas;
    }

    @GetMapping("/provincia-mas-reportes")
    public String obtenerProvinciaConMasReportes(@RequestParam(name = "idColeccion") Long idColeccion) {
        return servicioEstadisticas.obtenerProvinciaConMasReportes(idColeccion);
    }

    @GetMapping("/categoria-mas-reportes")
    public String obtenerCategoriaConMasReportes() {
        return servicioEstadisticas.obtenerCategoriaConMasReportes();
    }

    @GetMapping("/provincia-mas-reportes/{categoria}")
    public String obtenerProvinciaConMasReportesSegunCategoria(@PathVariable("categoria") String categoria) {
        return servicioEstadisticas.obtenerProvinciaConMasReportesSegunCategoria(categoria);
    }

    @GetMapping("/hora-pico/{categoria}")
    public Integer obtenerHoraDondeOcurrenMasReportesSegunCategoria(@PathVariable("categoria") String categoria) {
        return servicioEstadisticas.obtenerHoraDondeOcurrenMasReportesSegunCategoria(categoria);
    }

    @GetMapping("/solicitudes-spam")
    public Integer obtenerCantidadDeSolicitudesSpam() {
        return servicioEstadisticas.obtenerCantidadDeSolicitudesSpam();
    }

    @GetMapping("/exportar")
    public ResponseEntity<Resource> exportarEstadisticasCSV() {
        String filePath = servicioEstadisticas.exportarEstadisticasCSV("estadisticas.csv");

        Resource fileResource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(fileResource);
    }
}
