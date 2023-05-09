package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.validator.MovieValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MovieController {
    @Autowired MovieRepository movieRepository;
    @Autowired ArtistRepository artistRepository;

    @Autowired
    MovieValidator movieValidator;

    @GetMapping("/operazioniMovies")
    public String operazioniMovies(){
        return "operazioniMovies.html";
    }

    @GetMapping("/operazioniNews")
    public String operazioniNews(){
        return "operazioniNews.html";
    }
    @GetMapping("/formNewMovie")
    public String formNewMovie(Model model){
        Movie movie = new Movie();
        movie.setDirector(new Artist());
        model.addAttribute("movie", movie);
        return "formNewMovie.html";
    }
    @PostMapping("/addedMovie")
    public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
        movieValidator.validate(movie, bindingResult);
        if (!bindingResult.hasErrors()) {
            //movie.getDirector().getDirectedMovies().add(movie); //non serve piu, guarda setDirector()
            this.movieRepository.save(movie);
            model.addAttribute("movie", movie);
            return "movie.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo film esiste già, inseriscine uno nuovo :)");
            return "formNewMovie.html";
        }
    }
    @GetMapping("/movies/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        Movie movie = this.movieRepository.findById(id).get();

        //PER SINCRONIZZARCI SOL DATABASE ALL'INIZIO ye
        if(movie.getActors().size()==0) {
            movie.setActors(this.artistRepository.findAllByMoviesActedInIsContaining(movie));
            movieRepository.save(movie);
        }
        ////////////////////////////////////////////

        model.addAttribute("movie", movie);
        return "movie.html";
    }

    @GetMapping("/movies")
    public String showMovies(Model model) {
        model.addAttribute("movies", this.movieRepository.findAllByOrderByYearDesc());
        return "movies.html";
    }

    @GetMapping("/formSearchMovies")
    public String formSearchMovies(Model model) {
        return "formSearchMovies.html";
    }

    @PostMapping("/searchMovies")
    public String searchMovies(Model model, @RequestParam Integer year) {
        model.addAttribute("movies", this.movieRepository.findByYear(year));
        return "foundMovies.html";
    }

    @GetMapping("/gestisciMovies")
    public String gestisciMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAllByOrderByYearDesc());
        return "gestisciMovies.html";
    }
    @GetMapping("/formUpdateMovies/{id}")
    public String formUpdateMovies(@PathVariable("id") Long id, Model model) {
        Movie movie = movieRepository.findById(id).get();
        model.addAttribute("movie", movie);
        model.addAttribute("director", movie.getDirector());
        model.addAttribute("directors", artistRepository.findAll());
        return "formUpdateMovies.html";
    }
    @GetMapping("/directorsToAdd/{movieId}")
    public String showDirectorsList(@PathVariable("movieId") Long movieId, Model model){
        model.addAttribute("movie", movieRepository.findById(movieId).get());
        model.addAttribute("directors", artistRepository.findAll());
        return "directorsToAdd.html";
    }
    @GetMapping("/addDirectorToMovie/{movieId}/{dirId}")
    public String addDirectorToMovie(@PathVariable("movieId") Long movieId, @PathVariable("dirId") Long dirId, Model model){
        Movie movie = movieRepository.findById(movieId).get();
        Artist dir = artistRepository.findById(dirId).get();

        //dobbiamo rimuovere il film dai film diretti da quell'artista
        movie.getDirector().getDirectedMovies().remove(movie);

        movie.setDirector(dir);
        movieRepository.save(movie);
        model.addAttribute("movies", movieRepository.findAllByOrderByYearDesc());
        return "gestisciMovies.html";
    }

    @GetMapping("/allActorsForMovie/{movieId}")
    public String showActorListForMovie(@PathVariable("movieId") Long idM, Model model) {
        Movie movie = movieRepository.findById(idM).get();
        List<Artist> inMovieActors = artistRepository.findAllByMoviesActedInIsContaining(movie);
        model.addAttribute("movie", movie);
        //non puo esse che dobbiamo aggiornare tutto ogni volta, mi ammazzo, vabbe mo lo famo così
        //così il movie in caso c'ha i suoi attori
        if(movie.getActors() == null) {
            movie.setActors(new LinkedList<Artist>(inMovieActors));
            movieRepository.save(movie);
        }

        model.addAttribute("notInMovieActors", artistRepository.findAllByMoviesActedInIsNotContaining(movie));
        model.addAttribute("inMovieActors", inMovieActors);
        return "allActorsForMovie.html";
    }

    @GetMapping("/removeActorFromMovie/{movieId}/{actorId}")
    public String removeActorFromMovie(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM, Model model) {
        Movie movie = movieRepository.findById(idM).get();
        List<Artist> notInMovieActors = artistRepository.findAllByMoviesActedInIsNotContaining(movie);
        List<Artist> inMovieActors = artistRepository.findAllByMoviesActedInIsContaining(movie);

        Artist actorToRemove = artistRepository.findById(idA).get();
        inMovieActors.remove(actorToRemove);
        notInMovieActors.add(actorToRemove);
        movie.getActors().remove(actorToRemove);
        actorToRemove.getMoviesActedIn().remove(movie);

        movieRepository.save(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("notInMovieActors", notInMovieActors);
        model.addAttribute("inMovieActors", inMovieActors);
        return "allActorsForMovie.html";
    }

    @GetMapping("/addActorInMovie/{movieId}/{actorId}")
    public String addActorInMovie(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM, Model model) {
        Movie movie = movieRepository.findById(idM).get();
        List<Artist> notInMovieActors = artistRepository.findAllByMoviesActedInIsNotContaining(movie);
        List<Artist> inMovieActors = artistRepository.findAllByMoviesActedInIsContaining(movie);

        Artist actorToAdd = artistRepository.findById(idA).get();
        inMovieActors.add(actorToAdd);
        notInMovieActors.remove(actorToAdd);
        movie.getActors().add(actorToAdd);
        actorToAdd.getMoviesActedIn().add(movie);

        movieRepository.save(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("notInMovieActors", notInMovieActors);
        model.addAttribute("inMovieActors", inMovieActors);
        return "allActorsForMovie.html";
    }
}
