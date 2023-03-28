package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MovieController {
    @Autowired MovieRepository movieRepository;
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }
    @GetMapping("/formNewMovie")
    public String formNewMovie(Model model){
        model.addAttribute("movie", new Movie());
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
}
