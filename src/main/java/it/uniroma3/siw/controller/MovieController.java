package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {
    @Autowired MovieRepository movieRepository;
    @Autowired ArtistRepository artistRepository;
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }
    @GetMapping("/formNewMovie")
    public String formNewMovie(Model model){
        Movie movie = new Movie();
        movie.setDirector(new Artist());
        model.addAttribute("movie", movie);
        return "formNewMovie.html";
    }
    @PostMapping("/addedMovie")
    public String newMovie(@ModelAttribute("movie") Movie movie, Model model) {
        if (!movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())) {
            this.movieRepository.save(movie);
            model.addAttribute("movie", movie);
            return "movie.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo film esiste già");
            return "formNewMovie.html";
        }
    }
    @GetMapping("/movies/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", this.movieRepository.findById(id).get());
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
    @GetMapping("/operazioniMovies")
    public String operazioniMovies(){
        return "operazioniMovies.html";
    }
    @GetMapping("/gestisciMovies")
    public String gestisciMovies(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
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
        movie.setDirector(dir);
        movieRepository.save(movie);
        model.addAttribute("movies", movieRepository.findAll());
        return "gestisciMovies.html";
    }
}
