import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.*;

public class SpaceExplorerApp extends Application {

    private Graph<Planet> graph;
    private Pane root;

    private Planet start = null;
    private Planet end = null;

    private Map<Planet, Circle> nodeMap = new HashMap<>();
    private Map<Planet, List<Line>> edgeMap = new HashMap<>();
    private List<Line> pathLines = new ArrayList<>();

    private Button regenBtn;
    private Button clearPathBtn;

    @Override
    public void start(Stage stage){

        root = new Pane();
        graph = UniverseGenerator.generate(20);

        createRegenButton();
        createClearPathButton();

        redrawAll();

        stage.setScene(new Scene(root,1000,650));
        stage.setTitle("Galactic GPS");
        stage.show();
    }

    // ================= BUTTONS =================

    private void createRegenButton(){
        regenBtn = new Button("Regenerate Universe");
        regenBtn.setLayoutX(20);
        regenBtn.setLayoutY(20);
        regenBtn.setStyle("-fx-font-size: 14px; -fx-background-color: black; -fx-text-fill: white;");

        regenBtn.setOnAction(e -> {
            start = null;
            end = null;
            pathLines.clear();
            edgeMap.clear();
            graph = UniverseGenerator.generate(20);
            redrawAll();
        });
    }

    private void createClearPathButton() {
        clearPathBtn = new Button("Clear Path");
        clearPathBtn.setLayoutX(150);
        clearPathBtn.setLayoutY(20);
        clearPathBtn.setStyle("-fx-font-size: 14px; -fx-background-color: black; -fx-text-fill: white;");

        clearPathBtn.setOnAction(e -> {
            // Clear start/end selection
            start = null;
            end = null;

            // Remove gold path lines
            root.getChildren().removeAll(pathLines);
            pathLines.clear();

            // Keep original edges intact, just redraw
            redrawAll();
        });
    }

    // ================= DRAW EVERYTHING =================

    private void redrawAll(){

        root.getChildren().clear();
        nodeMap.clear();
        edgeMap.clear();

        drawGraph();
        redrawPath();

        // Keep buttons on top
        root.getChildren().addAll(regenBtn, clearPathBtn);
    }

    private void drawGraph(){

        Set<String> drawn = new HashSet<>();

        // DRAW EDGES
        for(Planet p : graph.getVertices()){
            for(Edge<Planet> e : graph.getEdges(p)){

                String key = e.getFrom()+"-"+e.getTo();
                String reverse = e.getTo()+"-"+e.getFrom();

                if(drawn.contains(key) || drawn.contains(reverse)) continue;

                drawn.add(key);

                Line line = new Line(
                        e.getFrom().getX(),
                        e.getFrom().getY(),
                        e.getTo().getX(),
                        e.getTo().getY()
                );

                if(e.getWeight() < 150){
                    line.setStroke(Color.GREEN);
                    line.setStrokeWidth(4);
                }
                else if(e.getWeight() > 300){
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(1);
                }
                else{
                    line.setStroke(Color.GRAY);
                }

                // Track lines for both planets
                edgeMap.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(line);
                edgeMap.computeIfAbsent(e.getTo(), k -> new ArrayList<>()).add(line);

                root.getChildren().add(line);
            }
        }

        // DRAW PLANETS
        for(Planet p : graph.getVertices()){

            Circle c = new Circle(p.getX(), p.getY(), 10);
            c.setFill(Color.CORNFLOWERBLUE);

            nodeMap.put(p, c);

            // ================= CLICK LOGIC =================
            c.setOnMouseClicked(e -> handleClick(p));

            // ================= SMOOTH DRAGGING =================
            c.setOnMouseDragged(e -> {
                c.toFront();
                c.setCenterX(e.getX());
                c.setCenterY(e.getY());

                p.setX(e.getX());
                p.setY(e.getY());

                // Update all connected edges to move with planet
                List<Line> lines = edgeMap.get(p);
                if(lines != null){
                    for(Line l : lines){
                        if(l.getStartX() == l.getStartX() && l.getStartY() == l.getStartY()){ // always true
                            // check which end is this planet
                            if(l.getStartX() != l.getEndX() || l.getStartY() != l.getEndY()) {
                                if(Math.hypot(l.getStartX()-p.getX(), l.getStartY()-p.getY()) < 20){
                                    l.setStartX(e.getX());
                                    l.setStartY(e.getY());
                                } else {
                                    l.setEndX(e.getX());
                                    l.setEndY(e.getY());
                                }
                            }
                        }
                    }
                }

                // Redraw gold path dynamically
                redrawPath();
            });

            c.setOnMouseEntered(e -> c.setStroke(Color.WHITE));
            c.setOnMouseExited(e -> c.setStroke(null));

            root.getChildren().add(c);
        }
    }

    // ================= CLICK LOGIC =================

    private void handleClick(Planet p){

        if(start == null){
            start = p;
        }
        else if(end == null){
            end = p;
        }
        else{
            start = p;
            end = null;
        }

        updateColors();
        redrawPath();
    }

    private void updateColors(){

        nodeMap.values().forEach(c -> c.setFill(Color.CORNFLOWERBLUE));

        if(start != null)
            nodeMap.get(start).setFill(Color.GREEN);

        if(end != null)
            nodeMap.get(end).setFill(Color.RED);
    }

    // ================= PATH =================

    private void redrawPath(){

        root.getChildren().removeAll(pathLines);
        pathLines.clear();

        if(start == null || end == null) return;

        List<Planet> path = Pathfinder.dijkstra(graph, start, end);

        for(int i=0;i<path.size()-1;i++){

            Planet a = path.get(i);
            Planet b = path.get(i+1);

            Line l = new Line(
                    a.getX(), a.getY(),
                    b.getX(), b.getY()
            );

            l.setStroke(Color.GOLD);
            l.setStrokeWidth(5);

            pathLines.add(l);
        }

        root.getChildren().addAll(pathLines);
    }

    public static void main(String[] args){
        launch();
    }
}
