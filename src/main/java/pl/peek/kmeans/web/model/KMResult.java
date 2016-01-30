package pl.peek.kmeans.web.model;


import pl.peek.kmeans.impl.Cluster;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
public class KMResult {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private final List<KMCluster> clusters;

    public KMResult() {
        clusters = Collections.emptyList();
    }

    public KMResult(List<KMCluster> clusters) {
        this.clusters = clusters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<KMCluster> getClusters() {
        return clusters;
    }
}
