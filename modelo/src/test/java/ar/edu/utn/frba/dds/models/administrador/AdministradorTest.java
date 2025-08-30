package ar.edu.utn.frba.dds.models.administrador;
import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.SolicitudEliminacion;
import ar.edu.utn.frba.dds.models.entities.administrador.Administrador;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;
import ar.edu.utn.frba.dds.models.entities.lectorCSV.LectorDeHechosCSV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AdministradorTest {
    @Test
    public void importarHechosRepetidos(){
        String path = getClass().getClassLoader().getResource("hechos_generados_repetidos.csv").getPath();
        LectorDeHechosCSV lector = new LectorDeHechosCSV();

        FuenteDataset fuenteDataset = new FuenteDataset(lector, path);

        fuenteDataset.importarHechos();

        Hecho hecho = fuenteDataset.getHechos().get(0);
        System.out.println("Primer hecho: " + hecho.getTitulo() + " Descripcion: " + hecho.getDescripcion() + " Categoria: " + hecho.getCategoria() + " Fecha del acontecimiento: " + hecho.getFecAcontecimiento());
        Hecho hecho2 = fuenteDataset.getHechos().get(1);
        System.out.println("Segundo hecho: " + hecho2.getTitulo() + " Descripcion: " + hecho2.getDescripcion() + " Categoria: " + hecho2.getCategoria() + " Fecha del acontecimiento: " + hecho2.getFecAcontecimiento());

        //En nuestro caso hay 10000 hechos en el csv pero como uno se repite terminan siendo 9999
        Assertions.assertEquals(9999, fuenteDataset.getHechos().size());
    }

    @Test
    public void importarHechosSinRepetidos(){
        String path = getClass().getClassLoader().getResource("hechos_generados.csv").getPath();
        LectorDeHechosCSV lector = new LectorDeHechosCSV();

        FuenteDataset fuenteDataset = new FuenteDataset(lector, path);

        fuenteDataset.importarHechos();

        Assertions.assertEquals(10000,fuenteDataset.getHechos().size());
    }

    @Test
    public void aprobarSolicitudEliminacion(){
        Administrador admin =new Administrador();
        String path = getClass().getClassLoader().getResource("hechos_generados.csv").getPath();
        LectorDeHechosCSV lector = new LectorDeHechosCSV();

        FuenteDataset fuenteDataset = new FuenteDataset(lector, path);

        fuenteDataset.importarHechos();
        String motivo = "Este hecho debe eliminarse".repeat(30);
        SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(motivo, fuenteDataset.getHechos().get(1).getId(), 1L);
        admin.aprobarSolicitudEliminacion(solicitudEliminacion);
        Assertions.assertEquals(true,fuenteDataset.getHechos().get(1).getEliminado());
    }
    @Test
    public void rechazarSolicitudEliminacion(){
        Administrador admin =new Administrador();
        String path = getClass().getClassLoader().getResource("hechos_generados.csv").getPath();
        LectorDeHechosCSV lector = new LectorDeHechosCSV();

        FuenteDataset fuenteDataset = new FuenteDataset(lector, path);

        fuenteDataset.importarHechos();
        String motivo = "Este hecho debe eliminarse".repeat(30);
        SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(motivo, fuenteDataset.getHechos().get(1).getId(), 1L);
        admin.rechazarSolicitudEliminacion(solicitudEliminacion);
        Assertions.assertEquals(false,fuenteDataset.getHechos().get(1).getEliminado());
    }
}