package ar.edu.utn.frba.dds.models.entities.lectorCSV;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorDeHechosCSV {
    public List<Hecho> obtenerHechos(String path, FuenteDataset fuente){
        List<Hecho> hechos = new ArrayList<>();
        String separador = ";";
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            String linea;
            boolean esPrimeraLinea = true;
            Hecho hecho;

            while ((linea = br.readLine()) != null) {
                // Saltamos la primera línea ya que son los encabezados
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }

                String[] columnas = linea.split(separador, -1); // -1 incluye vacíos

                // Accedo a columnas individuales por índice:
                String titulo = columnas[0];
                String descripcion = columnas[1];
                String categoria = columnas[2];
                String latitud = columnas[3];
                String longitud = columnas[4];
                String fechaDelHecho = columnas[5];

                Ubicacion ubicacion = new Ubicacion(latitud, longitud);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate fechaAcontecimiento = LocalDate.parse(fechaDelHecho, formatter);

                hecho = Hecho.of(titulo, descripcion, TipoHecho.TEXTO, categoria, ubicacion, fechaAcontecimiento, fuente);
                if(!hechoRepetido(hechos, hecho)){
                    hechos.add(hecho);
                }
                else{
                    this.reemplazarHechoRepetido(hechos, hecho);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return hechos;
    }

    public Boolean hechoRepetido(List<Hecho> hechos, Hecho hecho){
        return hechos.stream().anyMatch(h->h.getTitulo().equals(hecho.getTitulo()));
    }

    public Boolean hechoRepetido(Hecho hecho1, Hecho hecho2){
        return hecho1.getTitulo().equals(hecho2.getTitulo());
    }

    public void reemplazarHechoRepetido(List<Hecho> hechos, Hecho hecho){
        for (int i = 0; i < hechos.size(); i++) {
            Hecho h = hechos.get(i);
            //Si el título coincide, reemplazo el objeto en esa posición
            if (hechoRepetido(h, hecho)) {
                hechos.set(i, hecho);  //Reemplazo el objeto en la posición i
                break;
            }
        }
    }
}