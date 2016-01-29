package pl.peek.kmeans.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.peek.kmeans.web.form.UploadForm;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class KMeansController {

    private final KMeansService kMeansService;

    @Autowired
    public KMeansController(KMeansService kMeansService) {
        this.kMeansService = kMeansService;
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

        KMResult kmResult = null;
        try {
            kmResult = kMeansService.calculateResult(uploadForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("kmResult", kmResult);
        return "redirect:/results"; //TODO
    }

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    public String results(@ModelAttribute KMResult kmResult) {

        return "results";
    }

}
