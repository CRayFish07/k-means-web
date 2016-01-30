package pl.peek.kmeans.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.peek.kmeans.web.form.UploadForm;
import pl.peek.kmeans.web.model.KMResult;
import pl.peek.kmeans.web.repository.KMResultRepository;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class KMeansController {

    private final KMeansService kMeansService;
    private final KMResultRepository kmResultRepository;

    @Autowired
    public KMeansController(KMeansService kMeansService, KMResultRepository kmResultRepository) {
        this.kMeansService = kMeansService;
        this.kmResultRepository = kmResultRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(UploadForm uploadForm) {
        return "index";
    }

    @RequestMapping
    public String upload(@Valid UploadForm uploadForm, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (uploadForm.getFile() == null || uploadForm.getFile().getSize() == 0)
            bindingResult.rejectValue("file", "", "File must be present.");
        if (!"MET_A".equals(uploadForm.getMethod()) && !"MET_B".equals(uploadForm.getMethod()))
            bindingResult.rejectValue("method", "", "Unknown method type");

        if (bindingResult.hasErrors()) {
            return "index";
        }

        KMResult kmResult = kMeansService.calculateResult(uploadForm);
        if (kmResult == null) return "index";
        return "redirect:/result?id=" + kmResult.getId().intValue();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String results(@RequestParam("id") Long resultId, Model model) {
        KMResult one = kmResultRepository.findOne(resultId);
        model.addAttribute("result", one);
        return "result";
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String results(Model model) {
        model.addAttribute("results", kmResultRepository.findAll());
        return "results";
    }
}
