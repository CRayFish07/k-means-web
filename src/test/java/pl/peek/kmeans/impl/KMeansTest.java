package pl.peek.kmeans.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KMeansTest {

    private List<Point> squarePoints;

    @Before
    public void setUp() throws Exception {
        squarePoints = new ArrayList<>();
        addAll(
                this.squarePoints,
                new Point(0.0, 0.0),
                new Point(0.0, 1.0),
                new Point(1.0, 1.0),
                new Point(1.0, 0.0)
        );
    }

    @Test
    public void testClustersDoNotExceedRange() throws Exception {
        KMeans kMeans4 = new KMeans(4, squarePoints);
        kMeans4.calculateClusters();
        List<Cluster> clusters = kMeans4.getClusters();
        assertNotNull(clusters);
        clusters.forEach(c -> {
            double x = c.getCentroid().getX();
            double y = c.getCentroid().getY();
            assertTrue(x <= 1.0);
            assertTrue(x >= 0.0);
            assertTrue(y >= 0.0);
            assertTrue(y <= 1.0);
        });
    }

    @Test
    public void testMinMaxDoesNotExceedPointValues() throws Exception {
        KMeans kMeans1 = new KMeans(1, squarePoints);
        kMeans1.calculateClusters();
        assertTrue(kMeans1.getMinX().compareTo(0.0) == 0);
        assertTrue(kMeans1.getMinY().compareTo(0.0) == 0);

        assertTrue(kMeans1.getMaxX().compareTo(1.0) == 0);
        assertTrue(kMeans1.getMaxY().compareTo(1.0) == 0);
    }

    @Test
    public void testClusterListEqualsWantedValue() throws Exception {
        KMeans kMeans0 = new KMeans(0, this.squarePoints);
        kMeans0.calculateClusters();
        assertTrue(kMeans0.getClusters().isEmpty());
        KMeans kMeans1 = new KMeans(1, this.squarePoints);
        kMeans1.calculateClusters();
        assertTrue(kMeans1.getClusters().size() == 1);
        KMeans kMeans2 = new KMeans(2, this.squarePoints);
        kMeans2.calculateClusters();
        assertTrue(kMeans2.getClusters().size() == 2);
        KMeans kMeans4 = new KMeans(4, this.squarePoints);
        kMeans4.calculateClusters();
        assertTrue(kMeans4.getClusters().size() == 4);
    }

    @Test
    public void testPointDistribution() throws Exception {
        List<Cluster> clusters = new ArrayList<>();
        addAll(
                clusters,
                new Cluster(0.1, 0.1),
                new Cluster(0.1, 0.9),
                new Cluster(0.9, 0.9),
                new Cluster(0.9, 0.1)
        );
        KMeans kMeans = new KMeans();
        kMeans.distributePoints(clusters, this.squarePoints);
        //each cluster should have one point
        clusters.forEach(c -> assertTrue(c.getPoints().size() == 1));
        clusters.forEach(Cluster::clearPoints);
        squarePoints.set(0, new Point(0.0, 1.0));
        /*
            new Point(0.0, 1.0),
            new Point(0.0, 1.0),
            new Point(1.0, 1.0),
            new Point(1.0, 0.0)
         */
        //two points should go to one cluster
        kMeans.distributePoints(clusters, this.squarePoints);
        assertTrue(clusters.get(1).getPoints().size() == 2);
    }

    @Test
    public void testClusterCentroid() throws Exception {
        Cluster cluster = new Cluster();
        cluster.addPoints(squarePoints);

        Point point = KMeans.calculateCentroid(cluster);
        assertTrue(point.getX().compareTo(0.5) == 0);
        assertTrue(point.getY().compareTo(0.5) == 0);
    }

    @Test
    public void testCentroidChanged() throws Exception {
        Cluster cluster = new Cluster(0.0, 0.0);
        cluster.addPoints(squarePoints);

        assertTrue(KMeans.calculateCentroids(singletonList(cluster)));
        assertFalse(KMeans.calculateCentroids(singletonList(cluster)));
    }
}