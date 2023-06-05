package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.util.FileUploadUtil;
import it.uniroma3.siw.util.ModelPreparationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class GeneralController {

    @Autowired
    private CredentialsService credentialsService;
    @Autowired
    UserService userService;
    @Autowired
    MovieService movieService;
    @Autowired
    ArtistService artistService;
    @Autowired
    ModelPreparationUtil modelPreparationUtil;

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
        return modelPreparationUtil.prepareModelForIndexTemplate(
                "index.html",
                        model,
                        artistService.findAll(),
                        movieService.findAllByOrderByYearDesc());
    }

    @GetMapping("/success")
    public String defaultAfterLogin(Model model) {
        //in toeria non puo' essere null, ma per sicurezza
        if(userService.getCurrentUser() != null)
            FileUploadUtil.saveCurrentUserProfilePicForHeader(userService.getCurrentUser());
        return index(model);
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult, @Valid
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "formRegisterUser";
    }

    //lo user deve essere registrato o admin per accedere
    @GetMapping("/registered/profile")
    public String getProfilePage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "registered/profile.html";
    }

    @PostMapping("/registered/addedPic")
    public String addOrModifyProfilePic(@RequestParam("image")MultipartFile multipartFile, Model model) {
        User user = userService.getCurrentUser();
        try {
            userService.addImageToUser(user, multipartFile);
            FileUploadUtil.saveCurrentUserProfilePicForHeader(userService.getCurrentUser());
        } catch (IOException e) {
            model.addAttribute("erroreUpload", "Errore durante l'upload dell'immagine");
            return getProfilePage(model);
        }
        userService.saveUser(user);

        return getProfilePage(model);
    }

    @GetMapping("/registered/removeMovieToFavourites/{movieId}")
    public String removeMovieToFavourites(@PathVariable("movieId") Long movieId, Model model) {
        Movie movie = movieService.findById(movieId);
        User currentUser = userService.getCurrentUser();

        currentUser.getFavourites().remove(movie);

        return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
    }


    @GetMapping("/registered/addMovieToFavourites/{movieId}")
    public String addMovieToFavourites(@PathVariable("movieId") Long movieId, Model model) {
        Movie movie = movieService.findById(movieId);
        User currentUser = userService.getCurrentUser();

        currentUser.getFavourites().add(movie);

        return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
    }
}