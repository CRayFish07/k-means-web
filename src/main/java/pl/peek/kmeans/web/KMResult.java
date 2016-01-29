package pl.peek.kmeans.web;


import pl.peek.kmeans.impl.Cluster;

import java.util.Collections;
import java.util.List;

public class KMResult {

    private final List<Cluster> clusters;

    public KMResult() {
        clusters = Collections.emptyList();
    }

    public KMResult(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
