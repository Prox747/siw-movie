package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.util.ModelPreparationUtil;
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
    @Autowired
    ModelPreparationUtil modelPreparationUtil;

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
        Movie movie = movieService.findById(movieId);
        if (reviewService.existsByTitleAndReviewedMovie(review.getTitle(), movie)) {
            model.addAttribute("messaggioErrore", "Questa recensione ha un titolo che esiste già :(");
            model.addAttribute("movieId", movieId);
            return "registered/formAddReview.html";
        } else {
            reviewService.initializeAndSaveReview(rating, movieId, review);

            return modelPreparationUtil.prepareModelForMovieTemplate("movie.html", model, movie);
        }
    }

    @GetMapping("/admin/deleteReview/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Long reviewId,
                               Model model) {
        Review review = reviewService.findById(reviewId);
        Movie movie = review.getReviewedMovie();

        movie.getReviews().remove(review);
        review.getAuthor().setReview(null); //bad practice?

        reviewService.deleteById(reviewId);
        movieService.save(movie);

        return modelPreparationUtil.prepareModelForMovieTemplate("movie.html",model, movie);
    }
}
