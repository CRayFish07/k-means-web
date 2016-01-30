package pl.peek.kmeans.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.peek.kmeans.web.model.KMResult;

@Transactional
public interface KMResultRepository extends JpaRepository<KMResult, Long> {
}
