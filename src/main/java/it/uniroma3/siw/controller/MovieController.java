package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.util.ModelPreparationUtil;
import it.uniroma3.siw.validator.MovieValidator;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class MovieController {
    @Autowired
    MovieService movieService;
    @Autowired
    ArtistService artistService;
    @Autowired
    MovieValidator movieValidator;
    @Autowired
    ModelPreparationUtil modelPreparationUtil;

    @GetMapping("/admin/formNewMovie")
    public String formNewMovie(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie", movie);
        if(artistService.findAll().size() != 0)
            model.addAttribute("directors", artistService.findAll());
        return "admin/formNewMovie.html";
    }


    //we need this to return to the form with the error messages, and eliminate duplicate code
    public String returnToFormNewMovie(Model model){
        if(artistService.findAll().size() != 0)
            model.addAttribute("directors", artistService.findAll());
        return "admin/formNewMovie.html";
    }

    @PostMapping("/admin/addedMovie")
    public String newMovie(@RequestParam("image") MultipartFile multipartFile, @Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
        movieValidator.validate(movie, bindingResult);
        if (!bindingResult.hasErrors() && !multipartFile.isEmpty()) {
            try{
                movieService.addImageToMovie(movie, multipartFile);
            } catch (IOException e) {
                model.addAttribute("erroreUpload", "Errore nel caricamento dell'immagine");
                return returnToFormNewMovie(model);
            }

            if(movie.getDirector() != null){
                movie.getDirector().getDirectedMovies().add(movie);
            }
            this.movieService.save(movie);
            return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
        } else {
            return returnToFormNewMovie(model);
        }
    }

    @GetMapping("/admin/deleteMovie/{movieId}")
    public String deleteMovie(@PathVariable("movieId") Long movieId, Model model) {
        movieService.deleteMovie(movieId);
        return modelPreparationUtil.prepareModelForIndexTemplate(
                "index.html",
                model,
                artistService.findAll(),
                movieService.findAllByOrderByYearDesc());
    }

    @GetMapping("/movies/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        Movie movie = this.movieService.findById(id);

        //PER SINCRONIZZARCI COL DATABASE ALL'INIZIO ye
        if(movie.getActors().size()==0) {
            movie.setActors(this.artistService.actorsForMovie(movie));
            movieService.save(movie);
        }
        ////////////////////////////////////////////

        return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
    }

    @GetMapping("/admin/directorsToAdd/{movieId}")
    public String showDirectorsList(@PathVariable("movieId") Long movieId, Model model){
        model.addAttribute("movie", movieService.findById(movieId));
        model.addAttribute("directors", artistService.findAll());
        return "admin/directorsToAdd.html";
    }
    @GetMapping("/admin/addDirectorToMovie/{movieId}/{dirId}")
    public String addDirectorToMovie(@PathVariable("movieId") Long movieId, @PathVariable("dirId") Long dirId, Model model) throws NotFoundException {
        Movie movie = movieService.findById(movieId);
        Artist dir = artistService.findById(dirId);

        //dobbiamo rimuovere il film dai film diretti da quell'artista
        if(movie.getDirector() != null)
            movie.getDirector().getDirectedMovies().remove(movie);

        movie.setDirector(dir);
        movieService.save(movie);
        return modelPreparationUtil.prepareModelForMovieTemplate(
                "movie.html",
                        model,
                        movie);
    }

    @GetMapping("/admin/allActorsForMovie/{movieId}")
    public String showActorListForMovie(@PathVariable("movieId") Long idM, Model model) {
        Movie movie = movieService.findById(idM);
        Set<Artist> inMovieActors = artistService.actorsForMovie(movie);
        model.addAttribute("movie", movie);
        //così il movie in caso c'ha i suoi attori
        if(movie.getActors() == null) {
            movie.setActors(new HashSet<>(inMovieActors));
            movieService.save(movie);
        }

        model.addAttribute("notInMovieActors", artistService.actorsNotInMovie(movie));
        model.addAttribute("inMovieActors", inMovieActors);
        return "admin/allActorsForMovie.html";
    }

    @GetMapping("/admin/removeActorFromMovie/{movieId}/{actorId}")
    public String removeActorFromMovie(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM, Model model) throws NotFoundException {
        Movie movie = movieService.findById(idM);
        Set<Artist> notInMovieActors = artistService.actorsNotInMovie(movie);
        Set<Artist> inMovieActors = artistService.actorsForMovie(movie);

        Artist actorToRemove = artistService.findById(idA);
        inMovieActors.remove(actorToRemove);
        notInMovieActors.add(actorToRemove);
        movie.getActors().remove(actorToRemove);
        actorToRemove.getMoviesActedIn().remove(movie);

        movieService.save(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("notInMovieActors", notInMovieActors);
        model.addAttribute("inMovieActors", inMovieActors);
        return "admin/allActorsForMovie.html";
    }

    @GetMapping("/admin/addActorInMovie/{movieId}/{actorId}")
    public String addActorInMovie(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM, Model model) throws NotFoundException {
        Movie movie = movieService.findById(idM);
        Set<Artist> notInMovieActors = artistService.actorsNotInMovie(movie);
        Set<Artist> inMovieActors = artistService.actorsForMovie(movie);

        Artist actorToAdd = artistService.findById(idA);
        inMovieActors.add(actorToAdd);
        notInMovieActors.remove(actorToAdd);
        movie.getActors().add(actorToAdd);
        actorToAdd.getMoviesActedIn().add(movie);

        movieService.save(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("notInMovieActors", notInMovieActors);
        model.addAttribute("inMovieActors", inMovieActors);
        return "admin/allActorsForMovie.html";
    }
}

