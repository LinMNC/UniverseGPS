import java.util.*;

public class UniverseGenerator {

    public static Graph<Planet> generate(int count){

        Graph<Planet> graph = new Graph<>();
        Random rand = new Random();

        List<Planet> planets = new ArrayList<>();

        for(int i=0;i<count;i++){

            Planet p;
            boolean valid;

            do {
                valid = true;

                p = new Planet(
                        "P"+i,
                        50 + rand.nextInt(900),
                        50 + rand.nextInt(600)
                );

                for(Planet other : planets){
                    if(p.distanceTo(other) < 80){
                        valid = false;
                        break;
                    }
                }

            } while(!valid);

            planets.add(p);
            graph.addVertex(p);
        }

        for(Planet a : planets){

            List<Planet> others = new ArrayList<>(planets);
            others.remove(a);

            others.sort(
                    Comparator.comparingDouble(a::distanceTo)
            );

            for(int i=0;i<3;i++){
                Planet b = others.get(i);
                graph.addEdge(a,b,a.distanceTo(b));
            }
        }

        return graph;
    }
}
