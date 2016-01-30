package pl.peek.kmeans.web.model;

import pl.peek.kmeans.impl.Cluster;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class KMCluster {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @OneToMany(fetch = FetchType.EAGER)
    private List<KMPoint> points;

    public KMCluster() {
    }

    public KMCluster(Cluster cluster) {
        this.x = cluster.getCentroid().getX();
        this.y = cluster.getCentroid().getY();

        this.points = cluster.getPoints().stream().map(KMPoint::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public List<KMPoint> getPoints() {
        return points;
    }

    public void setPoints(List<KMPoint> points) {
        this.points = points;
    }
}
