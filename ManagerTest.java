import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    private Grafo grafo;
    private Manager manager;

    @BeforeEach
    public void setUp() {
        grafo = new Grafo();
        grafo.addEdge("A", "B", new double[]{10, 15, 20, 25});
        grafo.addEdge("B", "C", new double[]{5, 10, 15, 20});
        grafo.addEdge("A", "C", new double[]{30, 35, 40, 45});
        manager = new Manager(grafo);
        manager.runFloyd(); // clima por defecto: normal (índice 0)
    }

    @Test
    public void testCenterCity() {
        String center = manager.getCenter();
        assertNotNull(center);
        assertTrue(List.of("A", "B", "C").contains(center));
    }


    @Test
    public void testAddConnection() {
        manager.addConnection("C", "A", new double[]{8, 8, 8, 8});
        List<String> path = manager.getShortestPath("C", "A");
        assertEquals(List.of("C", "A"), path);
        assertEquals(8, manager.getShortestDistance("C", "A"));
    }

    @Test
    public void testInvalidClima() {
        assertThrows(IllegalArgumentException.class, () -> manager.setClima(5));
    }
}
