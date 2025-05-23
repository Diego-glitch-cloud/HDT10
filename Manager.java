import java.util.*;

public class Manager {
    private Grafo graph;
    private int currentClima;
    private double[][] dist;
    private int[][] next;

    /**
     * Constructor de la clase Manager.
     * @param graph grafo a gestionar
     */
    public Manager(Grafo graph) {
        this.graph = graph;
        this.currentClima = 0; // por defecto normal
        initFloydStructures();
    }

    private void initFloydStructures() {
        int n = graph.size();
        dist = new double[n][n];
        next = new int[n][n];
    }

    /**
     * Ejecuta el algoritmo de Floyd-Warshall para el clima actual.
     */
    public void runFloyd() {
        int n = graph.size();
        initFloydStructures();
        double[][] w = graph.getWeightMatrix(currentClima);
        // inicializar dist y next
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = w[i][j];
                if (i == j) dist[i][j] = 0;
                if (dist[i][j] < Double.POSITIVE_INFINITY) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }
        // Floyd
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    /**
     * Obtiene la ruta más corta entre dos ciudades.
     * @param from ciudad origen
     * @param to ciudad destino
     */
    public List<String> getShortestPath(String from, String to) {
        Integer u = graph.getCityIndex(from);
        Integer v = graph.getCityIndex(to);
        if (u == null || v == null || next[u][v] == -1) return Collections.emptyList();
        List<String> path = new ArrayList<>();
        int at = u;
        while (at != v) {
            path.add(graph.getCityByIndex(at));
            at = next[at][v];
        }
        path.add(graph.getCityByIndex(v));
        return path;
    }

    /**
     * Obtiene la distancia (peso) de la ruta más corta.
     * @param from ciudad origen
     * @param to ciudad destino
     */
    public double getShortestDistance(String from, String to) {
        Integer u = graph.getCityIndex(from);
        Integer v = graph.getCityIndex(to);
        if (u == null || v == null) return Double.POSITIVE_INFINITY;
        return dist[u][v];
    }

    /**
     * Calcula el centro del grafo (ciudad con mínima excentricidad).
     */
    public String getCenter() {
        int n = graph.size();
        double bestEcc = Double.POSITIVE_INFINITY;
        String centerCity = null;
        for (int i = 0; i < n; i++) {
            double ecc = 0;
            for (int j = 0; j < n; j++) {
                ecc = Math.max(ecc, dist[i][j]);
            }
            if (ecc < bestEcc) {
                bestEcc = ecc;
                centerCity = graph.getCityByIndex(i);
            }
        }
        return centerCity;
    }

    /**
     * Cambia el clima actual y recalcula Floyd.
     * @param clima nuevo clima (0: normal, 1: lluvia, 2: nieve, 3: tormenta)
     */
    public void setClima(int clima) {
        if (clima < 0 || clima >= Grafo.CLIMAS)
            throw new IllegalArgumentException("Clima inválido");
        this.currentClima = clima;
        runFloyd();
    }

    /**
     * Interrumpe la conexión entre dos ciudades.
     * @param from ciudad origen
     * @param to ciudad destino
     */
    public void interruptEdge(String from, String to) {
        graph.removeEdge(from, to);
        runFloyd();
    }

    /**
     * Agrega o actualiza una conexión con sus tiempos de viaje.
     * @param from ciudad origen
     * @param to ciudad destino
     * @param times tiempos de viaje para cada clima
     */
    public void addConnection(String from, String to, double[] times) {
        graph.addEdge(from, to, times);
        runFloyd();
    }

    /**
     * Modifica los tiempos de una arista.
     * @param from ciudad origen
     * @param to ciudad destino
     * @param times nuevos tiempos de viaje para cada clima
     */
    public void updateTimes(String from, String to, double[] times) {
        graph.updateEdgeTimes(from, to, times);
        runFloyd();
    }

    /**
     * Obtiene la matriz de distancias tras Floyd.
     */
    public double[][] getDistanceMatrix() {
        return dist;
    }
}