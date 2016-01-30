package pl.peek.kmeans.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.peek.kmeans.XlsPointsImporter;
import pl.peek.kmeans.impl.DistanceMethod;
import pl.peek.kmeans.impl.EuclidDistanceMethod;
import pl.peek.kmeans.impl.HmmDistanceMethod;
import pl.peek.kmeans.impl.KMeans;
import pl.peek.kmeans.web.form.UploadForm;
import pl.peek.kmeans.web.model.KMCluster;
import pl.peek.kmeans.web.model.KMResult;
import pl.peek.kmeans.web.repository.KMClusterRepository;
import pl.peek.kmeans.web.repository.KMPointRepository;
import pl.peek.kmeans.web.repository.KMResultRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.stream.Collectors.*;

@Service
public class KMeansService {

    private final KMResultRepository kmResultRepository;
    private final KMPointRepository kmPointRepository;
    private final KMClusterRepository kmClusterRepository;

    @Autowired
    public KMeansService(KMResultRepository kmResultRepository,
                         KMPointRepository kmPointRepository,
                         KMClusterRepository kmClusterRepository) {
        this.kmResultRepository = kmResultRepository;
        this.kmPointRepository = kmPointRepository;
        this.kmClusterRepository = kmClusterRepository;
    }

    public KMResult calculateResult(UploadForm uploadForm) throws IOException {
        DistanceMethod method = null;
        switch (uploadForm.getMethod()) {
            case "MET_A":
                method = new EuclidDistanceMethod();
                break;
            case "MET_B":
                method = new HmmDistanceMethod();
                break;
        }

        KMeans kMeans = new KMeans(
                uploadForm.getCount(),
                XlsPointsImporter
                        .convert(new ByteArrayInputStream(uploadForm.getFile().getBytes())),
                method
        );
        kMeans.calculateClusters();

        KMResult kmResult = new KMResult(kMeans.getClusters().stream().map(KMCluster::new)
                .collect(toList()));
        kmResult.getClusters().forEach(kmCluster -> {
            kmPointRepository.save(kmCluster.getPoints());
            kmClusterRepository.save(kmCluster);
        });
        return kmResultRepository.save(kmResult);
    }
}
