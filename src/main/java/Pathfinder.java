import java.util.*;

public class Pathfinder {

    public static List<Planet> dijkstra(
            Graph<Planet> graph,
            Planet start,
            Planet end){

        Map<Planet,Double> dist = new HashMap<>();
        Map<Planet,Planet> prev = new HashMap<>();

        for(Planet p : graph.getVertices())
            dist.put(p,Double.MAX_VALUE);

        dist.put(start,0.0);

        PriorityQueue<Planet> pq =
                new PriorityQueue<>(Comparator.comparing(dist::get));

        pq.add(start);

        while(!pq.isEmpty()){

            Planet current = pq.poll();

            if(current.equals(end)) break;

            for(Edge<Planet> e : graph.getEdges(current)){

                Planet next = e.getTo();
                double newDist =
                        dist.get(current)+e.getWeight();

                if(newDist < dist.get(next)){
                    dist.put(next,newDist);
                    prev.put(next,current);
                    pq.add(next);
                }
            }
        }

        if(!prev.containsKey(end) && !start.equals(end))
            return new ArrayList<>();

        List<Planet> path = new ArrayList<>();
        for(Planet at=end; at!=null; at=prev.get(at))
            path.add(0,at);

        return path;
    }
}
