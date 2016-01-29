package pl.peek.kmeans.impl;

public class EuclidDistanceMethod implements DistanceMethod {
    @Override
    public double calcDistance(Point p0, Point p1) {
        return Math.sqrt(
                Math.pow(p0.getX() - p1.getX(), 2) + Math.pow(p0.getY() - p1.getY(), 2)
        );
    }
}
