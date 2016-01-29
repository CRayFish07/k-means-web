package pl.peek.kmeans.web;

import org.springframework.stereotype.Service;
import pl.peek.kmeans.XlsPointsImporter;
import pl.peek.kmeans.impl.DistanceMethod;
import pl.peek.kmeans.impl.EuclidDistanceMethod;
import pl.peek.kmeans.impl.HmmDistanceMethod;
import pl.peek.kmeans.impl.KMeans;
import pl.peek.kmeans.web.form.UploadForm;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class KMeansService {

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

        return new KMResult(kMeans.getClusters());
    }
}
