public class Planet {

    private String name;
    private double x;
    private double y;

    // PATHFINDING STATE (Do not modify)
    public double minDistance = Double.MAX_VALUE;
    public Planet previous = null;
    public boolean visited = false;

    public Planet(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void reset() {
        minDistance = Double.MAX_VALUE;
        previous = null;
        visited = false;
    }

    // VISUAL WEIGHTING SUPPORT
    // distance between planets = edge weight
    public double distanceTo(Planet other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return name;
    }
}
