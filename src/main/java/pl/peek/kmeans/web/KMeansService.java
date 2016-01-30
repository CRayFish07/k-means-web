package pl.peek.kmeans.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.peek.kmeans.XlsPointsImporter;
import pl.peek.kmeans.impl.*;
import pl.peek.kmeans.web.form.UploadForm;
import pl.peek.kmeans.web.model.KMCluster;
import pl.peek.kmeans.web.model.KMResult;
import pl.peek.kmeans.web.repository.KMClusterRepository;
import pl.peek.kmeans.web.repository.KMPointRepository;
import pl.peek.kmeans.web.repository.KMResultRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
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

    /**
     * Calculate result based on valid form input.
     *
     * @param uploadForm Form
     * @return Result
     */
    public KMResult calculateResult(UploadForm uploadForm) {
        DistanceMethod method = getMethod(uploadForm.getMethod());
        KMeans kMeans = null;
        try {
            List<Point> points = XlsPointsImporter
                    .convert(new ByteArrayInputStream(uploadForm.getFile().getBytes()));
            kMeans = new KMeans(uploadForm.getCount(), points, method);
            kMeans.calculateClusters();
            save(kMeans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (kMeans != null)
            return save(kMeans);
        return null;
    }

    public KMResult save(KMeans kMeans) {
        KMResult kmResult = new KMResult(kMeans.getClusters().stream().map(KMCluster::new)
                .collect(toList()));
        kmResult.getClusters().forEach(kmCluster -> {
            kmPointRepository.save(kmCluster.getPoints());
            kmClusterRepository.save(kmCluster);
        });
        return kmResultRepository.save(kmResult);
    }

    private DistanceMethod getMethod(String method) {
        switch (method) {
            case "MET_A":
                return new EuclidDistanceMethod();
            case "MET_B":
                return new HmmDistanceMethod();
            default:
                throw new IllegalArgumentException("Unknown distance method type");
        }
    }
}
