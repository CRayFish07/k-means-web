package pl.peek.kmeans.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.peek.kmeans.XlsPointsImporter;
import pl.peek.kmeans.impl.DistanceMethod;
import pl.peek.kmeans.impl.EuclidDistanceMethod;
import pl.peek.kmeans.impl.HmmDistanceMethod;
import pl.peek.kmeans.impl.KMeans;
import pl.peek.kmeans.web.form.UploadForm;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class KMeansController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(UploadForm uploadForm) {
        return "index";
    }

    @RequestMapping
    public String upload(@Valid UploadForm uploadForm, BindingResult bindingResult) throws IOException {
        if (uploadForm.getFile() == null || uploadForm.getFile().getSize() == 0)
            bindingResult.rejectValue("file", "", "File must be present.");
        if (!"MET_A".equals(uploadForm.getMethod()) && !"MET_B".equals(uploadForm.getMethod()))
            bindingResult.rejectValue("method", "", "Unknown method type");

        if (bindingResult.hasErrors()) {
            return "index";
        }
        DistanceMethod method = null;
        switch (uploadForm.getMethod()) {
            case "MET_A":
                method = new EuclidDistanceMethod();
                break;
            case "MET_B":
                method = new HmmDistanceMethod();
                break;
        }

        KMeans kMeans = new KMeans(uploadForm.getCount(), XlsPointsImporter.convert(
                new ByteArrayInputStream(uploadForm.getFile().getBytes())), method);
        kMeans.calculateClusters();

        return "redirect:/results"; //TODO
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String results(KMeans kMeans) {
        return "results";
    }
}
