package ar.edu.utn.frba.dds.models.entities.lectorCSV;

import ar.edu.utn.frba.dds.models.entities.coleccion.Hecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.TipoHecho;
import ar.edu.utn.frba.dds.models.entities.coleccion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.fuente.FuenteDataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorDeHechosCSV {
    public LectorDeHechosCSV(FileReader fileReader) {
    }

    /*
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
    */
    public List<Hecho> obtenerHechos(String path, FuenteDataset fuente) {
        List<Hecho> hechos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            // Leemos la primera línea para detectar el separador
            String primeraLinea = br.readLine();
            if (primeraLinea == null) {
                return hechos; // Archivo vacío
            }

            String separador = detectarSeparador(primeraLinea);

            // Volvemos a abrir el archivo para leerlo desde el principio
            // (Esta es una forma simple, alternativas más complejas existen)
            try (BufferedReader brBody = new BufferedReader(new FileReader(path))) {
                String linea;
                boolean esPrimeraLinea = true;

                while ((linea = brBody.readLine()) != null) {
                    if (esPrimeraLinea) {
                        esPrimeraLinea = false;
                        continue; // Saltamos el encabezado
                    }

                    //String[] columnas = linea.split(separador, -1);
                    // Reemplazamos el split simple por uno que usa una expresión regular
                    // para manejar correctamente las comas dentro de campos entre comillas.
                    String[] columnas = linea.split(separador + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    // Verificación de seguridad para evitar errores
                    if (columnas.length < 6) {
                        System.err.println("[LectorCSV WARN] Línea ignorada por tener menos de 6 columnas: " + linea);
                        continue;
                    }

                    String titulo = columnas[0];
                    String descripcion = columnas[1];
                    String categoria = columnas[2];
                    String latitud = columnas[3];
                    String longitud = columnas[4];

                    Ubicacion ubicacion = new Ubicacion(latitud, longitud);
                    String fechaDelHecho = columnas[5];

                   // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDateTime fechaAcontecimiento = LocalDateTime.parse(fechaDelHecho.trim(), formatter);

                    Hecho hecho = Hecho.of(titulo, descripcion, TipoHecho.TEXTO, categoria, ubicacion, fechaAcontecimiento, fuente);
                    if (!hechoRepetido(hechos, hecho)) {
                        hechos.add(hecho);
                    } else {
                        this.reemplazarHechoRepetido(hechos, hecho);
                    }
                }
            }
        } catch (IOException e) {
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
    /**
     * Analiza una línea de texto (generalmente el encabezado) para determinar
     * si el delimitador es una coma o un punto y coma.
     * @param linea La línea a analizar.
     * @return El carácter delimitador detectado (',' o ';').
     */
    private String detectarSeparador(String linea) {
        long conteoComas = linea.chars().filter(ch -> ch == ',').count();
        long conteoPuntoYComa = linea.chars().filter(ch -> ch == ';').count();

        // Se asume que el separador más frecuente es el correcto.
        return (conteoComas > conteoPuntoYComa) ? "," : ";";
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