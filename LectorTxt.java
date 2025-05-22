package HDT10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorTxt {
    
    // Carga un grafo desde logistica.txt
    public static boolean cargarGrafo(String PathArchivo, GrafoAdyacencia grafo) {
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
    
    // revisa linea por linea el archivo
    private static boolean procesarLinea(String linea, GrafoAdyacencia grafo) {
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
            if (!grafo.containsNode(ciudad1)) {
                grafo.addNode(ciudad1);
            }
            if (!grafo.containsNode(ciudad2)) {
                grafo.addNode(ciudad2);
            }
            
            // Agrega la conección
            grafo.addArc(ciudad1, ciudad2, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
}