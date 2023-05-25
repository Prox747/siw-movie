package it.uniroma3.siw.util;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ModelPreparationUtil {
    @Autowired
    CredentialsService credentialsService;

    public String prepareModelForMovieTemplate(String template, Model modelToPrepare, Movie movieToInject) {
        modelToPrepare.addAttribute("movie", movieToInject);
        //se non siamo loggati
        if(!credentialsService.getCurrentCredentials().isPresent()) {
            return template;
        }
        Review currentUserReview = credentialsService.getCurrentCredentials().get().getUser().getReview();
        modelToPrepare.addAttribute("movie", movieToInject);
        //se è admin o è registrato e non ha ancora recensito il film, può aggiungere una recensione
        if(credentialsService.userIsAdmin()) {
            //se è admin può cancellare le recensioni
            modelToPrepare.addAttribute("userIsAdmin", true);
            if(currentUserReview == null)
                modelToPrepare.addAttribute("userCanAddReview", true);
        }
        if(credentialsService.userIsRegistered()) {
            if(currentUserReview == null)
                modelToPrepare.addAttribute("userCanAddReview", true);
        }
        return template;
    }
}
