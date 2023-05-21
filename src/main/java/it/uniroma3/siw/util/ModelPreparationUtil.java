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
        //se è admin o è registrato e non ha ancora recensito il film, può aggiungere una recensione
        if(credentialsService.userIsAdmin()) {
            //se è admin può cancellare le recensioni
            modelToPrepare.addAttribute("userIsAdmin", true);
            if(credentialsService.getCurrentCredentials().get().getReview() == null)
                modelToPrepare.addAttribute("userCanAddReview", true);
        }
        if(credentialsService.userIsRegistered()) {
            if(credentialsService.getCurrentCredentials().get().getReview() == null)
                modelToPrepare.addAttribute("userCanAddReview", true);
        }
    }
}
