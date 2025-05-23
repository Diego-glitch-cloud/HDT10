/**
 * La clase LectorTxt se encarga de leer un archivo de texto con información sobre conexiones entre ciudades 
 * y cargar dicha información en una estructura de grafo.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorTxt {
    
    /**
     * Carga un grafo desde un archivo de texto, en este caso, logistica.txt
     * @param PathArchivo Ruta del archivo 
     * @param grafo Objeto en el que se agregarán las ciudades y conexiones
     * @return true si una línea fue procesada o false en caso de error
     */
    public static boolean cargarGrafo(String PathArchivo, Grafo grafo) {

        int lineasProcesadas = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(PathArchivo))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                // Ignora las lneas vacias 
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                if (procesarLinea(linea, grafo)) {
                    lineasProcesadas++;
                }
            }
            
            return lineasProcesadas > 0;
            
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Procesa líneas del archivo, extrayendo las ciudades y los tiempos de conexión y agrega la información al grafo
     * @param linea indica la llínea del archivo a procesar
     * @param grafo Grafo donde se van a agregar las ciudades y conexiones
     * @return true si la línea fue válida o false si la línea causa algún error
     */
    private static boolean procesarLinea(String linea, Grafo grafo) {
        try {
            // Divide por cualquier tipo de espacio en blanco
            String[] partes = linea.trim().split("\\s+");
            
            // Valida que la línea tenga exactamente 6 campos
            if (partes.length != 6) {
                return false;
            }
            
            String ciudad1 = partes[0];
            String ciudad2 = partes[1];
            
            // Validaa que los nombres de ciudades no estén vacíos
            if (ciudad1.isEmpty() || ciudad2.isEmpty()) {
                return false;
            }
            
            // Convierte y valida los 4 tiempos
            int tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta;
            
            try {
                tiempoNormal = Integer.parseInt(partes[2]);
                tiempoLluvia = Integer.parseInt(partes[3]);
                tiempoNieve = Integer.parseInt(partes[4]);
                tiempoTormenta = Integer.parseInt(partes[5]);
                
                // verifica que noe xisten tiempos negativos
                if (tiempoNormal < 0 || tiempoLluvia < 0 || tiempoNieve < 0 || tiempoTormenta < 0) {
                    return false;
                }
                
            } catch (NumberFormatException e) {
                return false;
            }
            
            // agrega ciudades si no existen
            grafo.addCity(ciudad1);
            grafo.addCity(ciudad2);
            
            // Agrega la conección
            double[] tiempos = {
            tiempoNormal,
            tiempoLluvia,
            tiempoNieve,
            tiempoTormenta
            };

            grafo.addEdge(ciudad1, ciudad2, tiempos);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
}