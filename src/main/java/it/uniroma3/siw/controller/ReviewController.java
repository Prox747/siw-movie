package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    MovieService movieService;
    @Autowired
    UserService userService;

    @GetMapping("/registered/formAddReview/{movieId}")
    public String formAddReview(@PathVariable("movieId") Long movieId, Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("movieId", movieId);
        return "registered/formAddReview.html";
    }

    @PostMapping("/registered/addedReview/{movieId}")
    public String addedReview(@PathVariable("movieId") Long movieId,
                              @ModelAttribute("review") Review review,
                              @RequestParam("rating") int rating,
                              Model model) {
        if (reviewService.existsByTitle(review.getTitle())) {
            model.addAttribute("messaggioErrore", "Questa recensione ha un titolo che esiste già :(");
            return "registered/formAddReview.html";
        } else {

            reviewService.initializeAndSaveReview(rating, movieId, review);

            model.addAttribute("movie", movieService.findById(movieId));
            return "movie.html";
        }
    }
}
