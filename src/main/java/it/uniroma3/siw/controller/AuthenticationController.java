package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;

import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/register")
    public String showRegisterForm (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "formRegisterUser";
    }

    @GetMapping("/login")
    public String showLoginForm (Model model) {
        return "formLogin.html";
    }

    @GetMapping("/")
    public String index(Model model) {
        if (credentialsService.userIsAdmin()) {
            return "admin/indexAdmin";
        }
        else {
            return "index";
        }
    }

    @GetMapping("/success")
    public String defaultAfterLogin(Model model) {
        if (credentialsService.userIsAdmin()) {
            return "admin/indexAdmin";
        }
        else {
            return "index";
        }
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult, @Valid
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        //SBAGLIATO??? il prof ha messo "registerUser" invece di
        // "formRegisterUser" ma non esiste una pagina con quel nome
        //boh così sicuro funziona quindi daje
        return "formRegisterUser";
    }
}