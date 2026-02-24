import java.util.*;

public class UniverseGenerator {

    public static Graph<Planet> generate(int numPlanets) {

        Graph<Planet> graph = new Graph<>();
        Random rand = new Random();

        List<Planet> planets = new ArrayList<>();

        // create planets
        for (int i = 0; i < numPlanets; i++) {

            Planet p = new Planet(
                    "P" + i,
                    rand.nextDouble() * 800,
                    rand.nextDouble() * 600
            );

            planets.add(p);
            graph.addVertex(p);
        }

        // randomly connect planets
        for (int i = 0; i < planets.size(); i++) {
            for (int j = i + 1; j < planets.size(); j++) {

                if (rand.nextDouble() < 0.3) { // connection chance

                    Planet a = planets.get(i);
                    Planet b = planets.get(j);

                    // VISUAL WEIGHTING: distance = weight
                    double distance = a.distanceTo(b);

                    graph.addEdge(a, b, distance);
                }
            }
        }

        return graph;
    }
}