public class Planet {

    private String name;
    private double x;
    private double y;

    public Planet(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x){ this.x = x; }
    public void setY(double y){ this.y = y; }

    public double distanceTo(Planet other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public String toString() {
        return name;
    }

    // ensures HashMap works perfectly
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Planet)) return false;
        Planet p = (Planet)o;
        return name.equals(p.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }
}
