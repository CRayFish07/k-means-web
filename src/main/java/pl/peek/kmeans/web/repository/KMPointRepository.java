package pl.peek.kmeans.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.peek.kmeans.web.model.KMPoint;

@Repository
public interface KMPointRepository extends JpaRepository<KMPoint, Long> {
}
