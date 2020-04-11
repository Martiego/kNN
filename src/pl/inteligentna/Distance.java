package pl.inteligentna;

public class Distance implements Comparable<Distance> {
    private int id;
    private double distance;

    public Distance(int id, double distance) {
        this.id = id;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Distance distance) {
        return Double.compare(this.distance, distance.getDistance());
    }

    @Override
    public String toString() {
        return "Id: " + this.id + "\n"
                + "Distance: " + this.distance + "\n";
    }
}
