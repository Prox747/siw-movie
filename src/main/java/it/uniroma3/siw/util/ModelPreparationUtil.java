package it.uniroma3.siw.util;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ModelPreparationUtil {
    @Autowired
    CredentialsService credentialsService;

    public void prepareModelForMovieTemplate(Model modelToPrepare, Movie movieToInject) {
        modelToPrepare.addAttribute("movie", movieToInject);
        if(credentialsService.userIsAdmin()) {
            modelToPrepare.addAttribute("userIsAdmin", true);
        }
        if(credentialsService.userIsRegistered()) {
            if(credentialsService.getCurrentCredentials().get().getReview() == null)
                modelToPrepare.addAttribute("userCanAddReview", true);
        }
    }
}
