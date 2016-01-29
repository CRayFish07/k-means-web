package pl.peek.kmeans.impl;

public class Point {

    private double x;

    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public boolean isSame(Point point) {
        return getX().compareTo(point.getX()) == 0 &&
                getY().compareTo(point.getY()) == 0;
    }
}
