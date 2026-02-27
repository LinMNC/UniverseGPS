import java.util.*;

public class Graph<T> {

    private final Map<T, List<Edge<T>>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    // add planet/node
    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    // connect planets with weighted edge
    public void addEdge(T src, T des, double weight) {

        addVertex(src);
        addVertex(des);

        adjList.get(src).add(new Edge<>(src, des, weight));
        adjList.get(des).add(new Edge<>(des, src, weight));
    }

    // Navigator uses this for drawing + Dijkstra
    public List<Edge<T>> getEdges(T vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    public Set<T> getVertices() {
        return adjList.keySet();
    }


    // DASHBOARD BONUS SUPPORT

    // number of connections a planet has
    public int degree(T vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>()).size();
    }

    // degree distribution data for charts
    public Map<Integer, Integer> getDegreeDistribution() {

        Map<Integer, Integer> distribution = new HashMap<>();

        for (T vertex : adjList.keySet()) {
            int deg = degree(vertex);
            distribution.put(deg, distribution.getOrDefault(deg, 0) + 1);
        }

        return distribution;
    }


    public void resetNodes() {
        for (T node : getVertices()) {
            if (node instanceof Planet) {
                ((Planet) node).reset();
            }
        }
    }

    // useful helper (teachers like this)
    public int size() {
        return adjList.size();
    }
}