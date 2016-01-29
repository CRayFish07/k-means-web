package pl.peek.kmeans.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Cluster {

    private Point centroid;

    private final List<Point> points;

    public Cluster() {
        this.points = new ArrayList<>();
    }

    public Cluster(double x, double y) {
        this(new Point(x, y));
    }

    public Cluster(Point centroid) {
        this();
        this.centroid = centroid;
    }

    public List<Point> getPoints() {
        return Collections.unmodifiableList(this.points);
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    public void addPoints(List<Point> points) {
        this.points.addAll(points);
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public void clearPoints() {
        this.points.clear();
    }
}
