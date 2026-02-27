import java.awt.geom.Line2D;
import java.util.*;

public class UniverseGenerator {

    public static Graph<Planet> generate(int numPlanets) {

        Graph<Planet> graph = new Graph<>();
        Random rand = new Random();

        List<Planet> planets = new ArrayList<>();

        // create planets
        for (int i = 0; i < numPlanets; i++) {
            Boolean replace = false;
            Planet p = new Planet(
                    "P" + i,
                    rand.nextDouble() * 800,
                    rand.nextDouble() * 600
            );

            for (Planet neighbor : planets)
            {
                if (p.distanceTo(neighbor) <= 80)
                {
                    replace = true;
                    break;
                }
                else replace = false;
            }

            if (!replace)
            {
                planets.add(p);
                graph.addVertex(p);
            }
            else numPlanets += 1;
        }

        List<Edge<Planet>> addedEdges = new ArrayList<>();

        for (Planet a : planets) {

            List<Planet> nearestPlanets = new ArrayList<>();
            List<Double> nearestDistances = new ArrayList<>();

            for (Planet b : planets) {
                if (a == b) continue;

                double d = a.distanceTo(b);

                int pos = 0;
                while (pos < nearestDistances.size() && d > nearestDistances.get(pos)) {
                    pos++;
                }

                nearestPlanets.add(pos, b);
                nearestDistances.add(pos, d);

                if (nearestPlanets.size() > 3) {
                    nearestPlanets.remove(3);
                    nearestDistances.remove(3);
                }
            }

            // Try adding edges
            for (int i = 0; i < nearestPlanets.size(); i++) {

                Planet b = nearestPlanets.get(i);
                double weight = nearestDistances.get(i);

                boolean intersects = false;

                // Check intersection with existing edges
                for (Edge<Planet> e : addedEdges) {

                    Planet c = e.getFrom();
                    Planet d = e.getTo();

                    // Skip if they share a vertex
                    if (a == c || a == d || b == c || b == d)
                        continue;

                    if (segmentsIntersect(a, b, c, d)) {
                        intersects = true;
                        break;
                    }
                }

                if (!intersects) {

                    graph.addEdge(a, b, weight);
                    addedEdges.add(new Edge<>(a, b, weight));
                }
            }
        }


        /*
        for (Planet a : planets) {

            List<Planet> nearestPlanets = new ArrayList<>();
            List<Double> nearestDistances = new ArrayList<>();

            for (Planet b : planets) {
                if (a == b) continue;

                double d = a.distanceTo(b);

                // Insert into correct position (keep sorted)
                int pos = 0;
                while (pos < nearestDistances.size() && d > nearestDistances.get(pos)) {
                    pos++;
                }

                nearestPlanets.add(pos, b);
                nearestDistances.add(pos, d);

                // Keep only the 3 closest
                if (nearestPlanets.size() > 3) {
                    nearestPlanets.remove(3);
                    nearestDistances.remove(3);
                }
            }

            // Add edges for the 3 nearest
            for (int i = 0; i < nearestPlanets.size(); i++) {
                graph.addEdge(a, nearestPlanets.get(i), nearestDistances.get(i));
            }
        }

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
        */

        return graph;
    }

    // mostly AI-assisted methods
    private static boolean segmentsIntersect(Planet p1, Planet p2,
                                             Planet p3, Planet p4) {

        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();
        double x3 = p3.getX(), y3 = p3.getY();
        double x4 = p4.getX(), y4 = p4.getY();

        return linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    private static boolean linesIntersect(double x1, double y1,
                                          double x2, double y2,
                                          double x3, double y3,
                                          double x4, double y4) {
        return Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
    }
}

