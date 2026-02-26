import java.util.*;

public class Graph<T> {

    private Map<T, List<Edge<T>>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addVertex(T v) {
        adjList.putIfAbsent(v, new ArrayList<>());
    }

    // BIDIRECTIONAL EDGE rubric item
    public void addEdge(T a, T b, double weight) {

        addVertex(a);
        addVertex(b);

        adjList.get(a).add(new Edge<>(a, b, weight));
        adjList.get(b).add(new Edge<>(b, a, weight));
    }

    public List<Edge<T>> getEdges(T v) {
        return adjList.getOrDefault(v, new ArrayList<>());
    }

    public Set<T> getVertices() {
        return adjList.keySet();
    }

    public int degree(T v) {
        return adjList.get(v).size();
    }

    // dashboard bonus support
    public Map<Integer,Integer> getDegreeDistribution() {

        Map<Integer,Integer> dist = new HashMap<>();

        for(T v : adjList.keySet()) {
            int d = degree(v);
            dist.put(d, dist.getOrDefault(d,0)+1);
        }

        return dist;
    }
}