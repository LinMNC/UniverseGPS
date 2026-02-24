public class Planet {

    private String name;
    private double x;
    private double y;

    public Planet(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // ⭐ VISUAL WEIGHTING SUPPORT
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
