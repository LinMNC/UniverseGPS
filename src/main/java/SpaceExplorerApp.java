import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SpaceExplorerApp extends Application {

    private Pane spacePane;
    private Label statusLabel;
    private Graph<Planet> universe;
    private List<Line> pathLines = new ArrayList<>();
    private boolean isDragMode = false;

    class Delta { double x, y; }
    final Delta dragDelta = new Delta();

    private Planet startNode = null;
    private Planet endNode = null;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #111;"); // Space background

        // Setup Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setStyle("-fx-padding: 10px; -fx-background-color: #333;");
        Button btnGen = new Button("Generate Universe");
        Button btnClear = new Button("Clear Path");

        btnGen.setOnAction(e -> generateAndDraw());
        btnClear.setOnAction(e -> {
            startNode = null; endNode = null;
            drawGraph();
        });

        ToggleButton modeToggle = new ToggleButton("Switch to Drag Mode");
        modeToggle.setOnAction(event -> {
            isDragMode = modeToggle.isSelected();
            if (isDragMode) {
                modeToggle.setText("Switch to Click Mode");
            } else {
                modeToggle.setText("Switch to Drag Mode");
            }
        });

        toolbar.getChildren().addAll(btnGen, btnClear, modeToggle);

        // Setup Canvas
        spacePane = new Pane();
        statusLabel = new Label("Click 'Generate Universe'. If screen stays black, Graph.java is empty!");
        statusLabel.setStyle("-fx-text-fill: white; -fx-padding: 10px;");



        root.setCenter(spacePane);
        root.setTop(toolbar);
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Galactic Graph");
        stage.setScene(scene);
        stage.show();
    }

    private void generateAndDraw() {
        // This calls the Generator (which currently returns Earth/Mars)
        universe = UniverseGenerator.generate(20);
        startNode = null;
        endNode = null;
        drawGraph();
    }

    private void drawGraph() {
        spacePane.getChildren().clear();

        if (universe == null || universe.getVertices().isEmpty()) {
            statusLabel.setText("Error: Universe is empty. Did Student A implement Graph.addVertex?");
            return;
        }

        java.util.Set<String> drawnEdges = new java.util.HashSet<>();

        for (Planet p : universe.getVertices()) {
            for (Edge<Planet> neighbor: universe.getEdges(p)) {
                // Create a stable key for the pair (p, neighbor)
                String id1 = String.valueOf(System.identityHashCode(p));
                String id2 = String.valueOf(System.identityHashCode(neighbor));
                String key = (id1.compareTo(id2) <= 0) ? (id1 + "-" + id2) : (id2 + "-" + id1);

                if (drawnEdges.add(key)) {
                    Line line = new Line(p.getX(), p.getY(), neighbor.getTo().getX(), neighbor.getTo().getY());
                    line.setStroke(Color.GRAY);
                    line.setStrokeWidth(2);
                    spacePane.getChildren().add(line);
                }
            }
        }

        // 2) Draw Nodes (Planets) — blue (Circle objects)
        for (Planet p : universe.getVertices()) {
            Circle circle = new Circle(p.getX(), p.getY(), 10);
            circle.setFill(Color.DODGERBLUE);
            circle.setStroke(Color.DEEPSKYBLUE);
            circle.setStrokeWidth(1.5);

            // 3) Add Click Events
            circle.setOnMouseClicked(e -> handlePlanetClick(p));

            circle.setOnMouseDragged(e -> {
                if (isDragMode) {
                    circle.toFront();
                    circle.setCenterX(e.getX());
                    circle.setCenterY(e.getY());

                    p.setX(e.getX());
                    p.setY(e.getY());

                    // Update all connected edges to move with planet
                    List<Line> lines = pathLines;
                    if (lines != null) {
                        for (Line l : lines) {
                            if (l.getStartX() == l.getStartX() && l.getStartY() == l.getStartY()) { // always true
                                // check which end is this planet
                                if (l.getStartX() != l.getEndX() || l.getStartY() != l.getEndY()) {
                                    if (Math.hypot(l.getStartX() - p.getX(), l.getStartY() - p.getY()) < 20) {
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
                    drawPath();
                }
            });

            circle.setOnMouseReleased(e -> {
                if (isDragMode) {
                    drawGraph();
                }
            });

            circle.setOnMouseEntered(e->circle.setStroke(Color.WHITE));

            circle.setOnMouseExited(e-> {
                circle.setStroke(null);
            });


            spacePane.getChildren().add(circle);
        }

        // --- STUDENT B TASK: DRAW THE GRAPH ---

        // 1. Draw Edges (Lines)
        // TODO: Loop through every planet in universe.getVertices()
        // TODO: Loop through every neighbor in universe.getNeighbors(planet)
        // TODO: Create a new Line(p.x, p.y, neighbor.x, neighbor.y)
        // TODO: spacePane.getChildren().add(line);

        // 2. Draw Nodes (Planets)
        // TODO: Loop through every planet again.
        // TODO: Create a Circle(p.x, p.y, 10)
        // TODO: Set color Color.CYAN
        // TODO: spacePane.getChildren().add(circle);

        // 3. Add Click Events
        // circle.setOnMouseClicked(e -> handlePlanetClick(p));


    }

    private void handlePlanetClick(Planet p) {
        if (startNode == null) {
            startNode = p;
            statusLabel.setText("Start: " + p.getName());
        } else if (endNode == null && p != startNode) {
            endNode = p;
            statusLabel.setText("Calculating path...");
            drawPath();
        }
    }

    private void drawPath() {
        pathLines.clear();
        List<Planet> path = Pathfinder.findShortestPath(universe, startNode, endNode);

        for(int i=0; i < path.size()-1; i++) {

            Planet a = path.get(i);
            Planet b = path.get(i+1);

            Line l = new Line(
                    a.getX(),
                    a.getY(),
                    b.getX(),
                    b.getY());

            l.setStroke(Color.GOLD);
            l.setStrokeWidth(5);

            pathLines.add(l);
        }
        spacePane.getChildren().addAll(pathLines);

        // --- STUDENT B TASK: DRAW THE PATH ---
        // TODO: Loop through the 'path' list.
        // TODO: Draw a thick GOLD line connecting the planets.

        if (path.isEmpty()) {
            statusLabel.setText("No path found.");
        } else {
            statusLabel.setText("Path found! Steps: " + (path.size()-1));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}