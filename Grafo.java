import java.util.*;

// Clase que representa un grafo dirigido con pesos variables según clima.
public class Grafo {
    public static final int CLIMAS = 4; // 0: normal, 1: lluvia, 2: nieve, 3: tormenta
    private Map<String,Integer> cityIndex;
    private List<String> cities;
    private double[][][] weights; // [clima][i][j]
    /*
    *   Constructor de la clase Grafo.
    */
    public Grafo() {
        this.cityIndex = new HashMap<>();
        this.cities = new ArrayList<>();
        this.weights = new double[CLIMAS][0][0];
    }

    /**
     * Añade un nodo (ciudad) si no existe.
     * @param city nombre de la ciudad
     */
    public void addCity(String city) {
        if (!cityIndex.containsKey(city)) {
            int newIndex = cities.size();
            cities.add(city);
            cityIndex.put(city, newIndex);
            expandMatrices();
        }
    }

    /**
     * Expande las matrices de pesos al agregar un nuevo nodo.
     */
    private void expandMatrices() {
        int n = cities.size();
        double[][][] newW = new double[CLIMAS][n][n];
        for (int c = 0; c < CLIMAS; c++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i < weights[c].length && j < weights[c].length) {
                        newW[c][i][j] = weights[c][i][j];
                    } else {
                        newW[c][i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        this.weights = newW;
    }

    /**
     * Añade una arista dirigida con los pesos según clima.
     * @param from ciudad origen
     * @param to ciudad destino
     * @param times array de tamaño 4 con tiempos para cada clima
     */
    public void addEdge(String from, String to, double[] times) {
        addCity(from);
        addCity(to);
        int i = cityIndex.get(from);
        int j = cityIndex.get(to);
        for (int c = 0; c < CLIMAS; c++) {
            weights[c][i][j] = times[c];
        }
    }

    /**
     * Elimina la arista entre dos ciudades.
     * @param from ciudad origen
     * @param to ciudad destino
     */
    public void removeEdge(String from, String to) {
        Integer i = cityIndex.get(from);
        Integer j = cityIndex.get(to);
        if (i != null && j != null) {
            for (int c = 0; c < CLIMAS; c++) {
                weights[c][i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    /**
     * Actualiza los tiempos (pesos) de la arista para todos los climas.
     * @param from ciudad origen
     * @param to ciudad destino
     * @param times array de tamaño 4 con tiempos para cada clima
     */
    public void updateEdgeTimes(String from, String to, double[] times) {
        addEdge(from, to, times);
    }

    /**
     * Obtiene el número de nodos en el grafo.
     */
    public int size() {
        return cities.size();
    }

    /**
     * Devuelve la lista de ciudades índice-based.
     */
    public List<String> getCities() {
        return Collections.unmodifiableList(cities);
    }

    /**
     * Devuelve el índice de una ciudad.
     * @param city nombre de la ciudad
     */
    public Integer getCityIndex(String city) {
        return cityIndex.get(city);
    }

    /**
     * Devuelve la matriz de pesos para un clima específico.
     * @param clima índice del clima (0: normal, 1: lluvia, 2: nieve, 3: tormenta)
     */
    public double[][] getWeightMatrix(int clima) {
        return weights[clima];
    }

    /**
     * Devuelve la ciudad dado su índice.
     * @param idx índice de la ciudad
     */
    public String getCityByIndex(int idx) {
        return cities.get(idx);
    }
}

