package pl.peek.kmeans.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.peek.kmeans.web.model.KMCluster;

@Repository
public interface KMClusterRepository extends JpaRepository<KMCluster, Long> {
}
