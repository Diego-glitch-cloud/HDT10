/*
 * Autores: Pedro Caso y Diego Calderón
 * trabajo: HDT10
 * Clase: Estructura de datos y algoritmos
 * fecha de modificacion: 22/05/2025
 */

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Grafo grafo = new Grafo();

        // Cargar el grafo desde logistica.txt
        boolean cargado = LectorTxt.cargarGrafo("logistica.txt", grafo);
        if (!cargado) {
            System.out.println("Error al cargar el archivo logistica.txt");
            return;
        }

        // Inicializar manager con clima normal (índice 0)
        Manager manager = new Manager(grafo);
        manager.setClima(0);

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n Conexion entre ciudades");
            System.out.println("1. Consultar ruta mas corta entre dos ciudades");
            System.out.println("2. Ver ciudad centro del grafo");
            System.out.println("3. Modificar grafo");
            System.out.println("4. Salir");
            System.out.print("Escoja una de las opciones: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1" -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine();

                    List<String> ruta = manager.getShortestPath(origen, destino);
                    double distancia = manager.getShortestDistance(origen, destino);

                    if (ruta.isEmpty() || distancia == Double.POSITIVE_INFINITY) {
                        System.out.println("No hay ruta disponible entre esas ciudades.");
                    } else {
                        System.out.println("Ruta: " + String.join(" -> ", ruta));
                        System.out.println("Distancia: " + distancia);
                    }
                }

                case "2" -> {
                    String centro = manager.getCenter();
                    System.out.println("Ciudad centro del grafo: " + centro);
                }

                case "3" -> {
                    boolean modificar = true;
                    while (modificar) {
                        System.out.println("\n Modificacion de un grafo");
                        System.out.println("a. Interrumpir conexion entre ciudades");
                        System.out.println("b. Agregar o modificar conexión");
                        System.out.println("c. Cambiar clima actual");
                        System.out.println("d. Volver al menú principal");
                        System.out.print("Opción: ");
                        String subopcion = sc.nextLine().toLowerCase();

                        switch (subopcion) {
                            case "a" -> {
                                System.out.print("Ciudad origen: ");
                                String from = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String to = sc.nextLine();
                                manager.interruptEdge(from, to);
                                System.out.println("Conexión interrumpida y rutas actualizadas.");
                            }

                            case "b" -> {
                                System.out.print("Ciudad origen: ");
                                String from = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String to = sc.nextLine();
                                double[] tiempos = new double[4];
                                String[] climas = {"normal", "lluvia", "nieve", "tormenta"};
                                for (int i = 0; i < 4; i++) {
                                    System.out.print("Tiempo en clima " + climas[i] + ": ");
                                    tiempos[i] = Double.parseDouble(sc.nextLine());
                                }
                                manager.addConnection(from, to, tiempos);
                                System.out.println("Conexión agregada o actualizada. Rutas recalculadas.");
                            }

                            case "c" -> {
                                System.out.println("Seleccione el clima actual:");
                                System.out.println("0: Normal");
                                System.out.println("1: Lluvia");
                                System.out.println("2: Nieve");
                                System.out.println("3: Tormenta");
                                System.out.print("Clima: ");
                                int clima = Integer.parseInt(sc.nextLine());
                                try {
                                    manager.setClima(clima);
                                    System.out.println("Clima actualizado y rutas recalculadas.");
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Clima inválido.");
                                }
                            }

                            case "d" -> modificar = false;

                            default -> System.out.println("Opción inválida del submenú.");
                        }
                    }
                }

                case "4" -> {
                    continuar = false;
                    System.out.println("saliendo...");
                }

                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }

        sc.close();
    }
}
