import java.util.*;

public class Graph<T> {

    private Map<T, List<Edge<T>>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    // add planet/node
    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    // connect planets with weighted edge
    public void addEdge(T from, T to, double weight) {

        addVertex(from);
        addVertex(to);

        adjList.get(from).add(new Edge<>(from, to, weight));
        adjList.get(to).add(new Edge<>(to, from, weight)); // undirected graph
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
            distribution.put(deg,
                    distribution.getOrDefault(deg, 0) + 1);
        }

        return distribution;
    }

    // useful helper (teachers like this)
    public int size() {
        return adjList.size();
    }
}