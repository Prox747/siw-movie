package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
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
import java.util.LinkedList;
import java.util.List;

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

    @GetMapping("/admin/operazioniMovies")
    public String operazioniMovies(){
        return "admin/operazioniMovies.html";
    }

    @GetMapping("/admin/operazioniArtisti")
    public String operazioniArtisti(){
        return "admin/operazioniArtisti.html";
    }
    @GetMapping("/admin/formNewMovie")
    public String formNewMovie(Model model){
        Movie movie = new Movie();
        model.addAttribute("movie", movie);
        model.addAttribute("directors", artistService.findAll());
        return "admin/formNewMovie.html";
    }
    @PostMapping("/admin/addedMovie")
    public String newMovie(@Valid @ModelAttribute("movie") Movie movie, @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) throws IOException {
        movieValidator.validate(movie, bindingResult);
        if (!bindingResult.hasErrors()) {

            movieService.addImageToMovie(movie, multipartFile);

            if(movie.getDirector() != null){
                movie.getDirector().getDirectedMovies().add(movie);
            }
            this.movieService.save(movie);
            return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
        } else {
            model.addAttribute("messaggioErrore", "Questo film esiste già, inseriscine uno nuovo :)");
            return "admin/formNewMovie.html";
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

    @GetMapping("/formSearchMovies")
    public String formSearchMovies(Model model) {
        return "formSearchMovies.html";
    }

    @PostMapping("/searchMovies")
    public String searchMovies(Model model, @RequestParam Integer year) {
        model.addAttribute("movies", this.movieService.findByYear(year));
        //per dire di che anno sono i film (ripeitiamo l'anno che ci hanno dato)
        model.addAttribute("year", year);
        return "foundMovies.html";
    }

    @GetMapping("/admin/gestisciMovies")
    public String gestisciMovies(Model model) {
        model.addAttribute("movies", movieService.findAllByOrderByYearDesc());
        return "admin/gestisciMovies.html";
    }
    @GetMapping("/admin/formUpdateMovies/{id}")
    public String formUpdateMovies(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.findById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("director", movie.getDirector());
        return "admin/formUpdateMovies.html";
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
        List<Artist> inMovieActors = artistService.actorsForMovie(movie);
        model.addAttribute("movie", movie);
        //così il movie in caso c'ha i suoi attori
        if(movie.getActors() == null) {
            movie.setActors(new LinkedList<>(inMovieActors));
            movieService.save(movie);
        }

        model.addAttribute("notInMovieActors", artistService.actorsNotInMovie(movie));
        model.addAttribute("inMovieActors", inMovieActors);
        return "admin/allActorsForMovie.html";
    }

    @GetMapping("/admin/removeActorFromMovie/{movieId}/{actorId}")
    public String removeActorFromMovie(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM, Model model) throws NotFoundException {
        Movie movie = movieService.findById(idM);
        List<Artist> notInMovieActors = artistService.actorsNotInMovie(movie);
        List<Artist> inMovieActors = artistService.actorsForMovie(movie);

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
        List<Artist> notInMovieActors = artistService.actorsNotInMovie(movie);
        List<Artist> inMovieActors = artistService.actorsForMovie(movie);

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
