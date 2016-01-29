package pl.peek.kmeans.impl;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class KMeans {
    private int clusterCount;

    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;

    private List<Point> points;
    private List<Cluster> clusters;

    private final DistanceMethod distanceMethod;

    private final static int REPOSITION_LIMIT = 10;

    public KMeans() {
        this(0, Collections.emptyList());
    }

    public KMeans(int clusterCount, List<Point> points) {
        this(clusterCount, points, new EuclidDistanceMethod());
    }

    public KMeans(int clusterCount, List<Point> points, DistanceMethod distanceMethod) {
        if (clusterCount > points.size())
            throw new IllegalArgumentException("To many clusters required for given point list.");

        this.clusterCount = clusterCount;
        this.points = points;
        this.distanceMethod = distanceMethod;
    }

    public void calculateClusters() {
        computeMinMax();

        this.clusters = makeClusters(clusterCount, points);
        calculateCentroids();

        distributePoints();
        boolean calc = true;
        int repositions = 0;
        while (calc) {
            clearClusters();
            distributePoints();
            calc = calculateCentroids();
            if (!calc && hasEmptyClusters() && repositions < REPOSITION_LIMIT) {
                calc = true;
                repositions++;
                setRandomPosition(this.clusters.stream().filter(c -> c.getPoints().isEmpty())
                        .findFirst().get());
            }
        }
    }

    private boolean hasEmptyClusters() {
        return hasEmptyClusters(this.clusters);
    }

    private boolean hasEmptyClusters(List<Cluster> clusters) {
        return clusters.stream().anyMatch(c -> c.getPoints().isEmpty());
    }

    private void clearClusters() {
        clearClusters(this.clusters);
    }

    private boolean calculateCentroids() {
        return calculateCentroids(this.clusters);
    }

    public static boolean calculateCentroids(List<Cluster> clusters) {
        Map<Cluster, Point> centroids = clusters.stream()
                .collect(toMap(c -> c, KMeans::calculateCentroid));
        boolean ret = centroids.entrySet().stream()
                .anyMatch(e -> !e.getKey().getCentroid().isSame(e.getValue()));
        centroids.entrySet().forEach(e -> e.getKey().setCentroid(e.getValue()));
        return ret;
    }

    public static Point calculateCentroid(Cluster cluster) {
        double sumX = cluster.getPoints().stream().mapToDouble(Point::getX).sum();
        double sumY = cluster.getPoints().stream().mapToDouble(Point::getY).sum();

        return new Point(sumX / cluster.getPoints().size(), sumY / cluster.getPoints().size());
    }

    private void clearClusters(List<Cluster> clusters) {
        clusters.stream().forEach(Cluster::clearPoints);
    }

    private void computeMinMax() {
        minX = computeMinX(this.points);
        maxX = computeMaxX(this.points);
        minY = computeMinY(this.points);
        maxY = computeMaxY(this.points);
    }

    public static double computeMinX(List<Point> points) {
        return points.stream().map(Point::getX).min(Double::compareTo).orElse(0.0);
    }

    public static double computeMaxX(List<Point> points) {
        return points.stream().map(Point::getX).max(Double::compareTo).orElse(0.0);
    }

    public static double computeMinY(List<Point> points) {
        return points.stream().map(Point::getY).min(Double::compareTo).orElse(0.0);
    }

    public static double computeMaxY(List<Point> points) {
        return points.stream().map(Point::getY).max(Double::compareTo).orElse(0.0);
    }

    private List<Cluster> makeClusters(int clusterCount, List<Point> points) {
        if (clusterCount == 0) return Collections.emptyList();
        List<Cluster> clusters = initClusters(clusterCount);
        sharePointsBetweenClusters(clusters, points);
        return clusters;
    }

    private void sharePointsBetweenClusters(List<Cluster> clusters, List<Point> points) {
        int c = 0;
        for (Point point : points) {
            clusters.get(c).addPoint(point);
            c++;
            if (c + 1 >= clusterCount) c = 0;
        }
    }

    private List<Cluster> initClusters(int n) {
        return initClusters(n, this.minX, this.minY, this.maxX, this.maxY);
    }

    private List<Cluster> initClusters(int n, double minX, double minY, double maxX, double maxY) {
        List<Cluster> clusters = new ArrayList<>();
        if (n == 0) return clusters;
        for (int i = 0; i < n; i++) {
            Cluster cluster = new Cluster();
            setRandomPosition(cluster);
            clusters.add(cluster);
        }
        return clusters;
    }

    private void setRandomPosition(Cluster cluster) {
        setRandomPosition(cluster, minX, minY, maxX, maxY);
    }

    public static void setRandomPosition(Cluster cluster, double minX, double minY, double maxX,
                                         double maxY) {
        cluster.setCentroid(computeRandomPosition(minX, minY, maxX, maxY));
    }

    public static Point computeRandomPosition(double minX, double minY, double maxX, double maxY) {
        Random r = new Random();
        return new Point(
                minX + (maxX - minX) * r.nextDouble(), minY + (maxY - minY) * r.nextDouble()
        );
    }

    private void distributePoints() {
        distributePoints(this.clusters, this.points);
    }

    public void distributePoints(List<Cluster> clusters, List<Point> points) {
        distributePoints(clusters, points, this.distanceMethod);
    }

    public static void distributePoints(List<Cluster> clusters, List<Point> points,
                                        DistanceMethod calc) {
        points.forEach(p -> clusters
                .stream()
                .min((o1, o2) -> distance(o1, p, calc).compareTo(distance(o2, p, calc)))
                .ifPresent(cluster -> cluster.addPoint(p)));
    }

    public static Double distance(Cluster cluster, Point point, DistanceMethod method) {
        return distance(cluster.getCentroid(), point, method);
    }

    public static Double distance(Point p0, Point p1, DistanceMethod method) {
        return method.calcDistance(p0, p1);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public Double getMinX() {
        return minX;
    }

    public Double getMaxX() {
        return maxX;
    }

    public Double getMinY() {
        return minY;
    }

    public Double getMaxY() {
        return maxY;
    }
}