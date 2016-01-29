package pl.peek.kmeans.impl;

public class HmmDistanceMethod implements DistanceMethod {
    @Override
    public double calcDistance(Point p0, Point p1) {
        return Math.min(Math.abs(p0.getX() - p1.getX()), Math.abs(p0.getY() - p1.getY()));
    }
}
