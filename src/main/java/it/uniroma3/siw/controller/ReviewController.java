package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.FileUploadUtil;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserService userService;

    @GetMapping("/registered/formAddReview/{movieId}")
    public String formAddReview(@PathVariable("movieId") Long movieId, Model model) {
        Movie movieInReview = this.movieRepository.findById(movieId).get();
        model.addAttribute("movie", movieInReview);
        return "registered/formAddReview.html";
    }

    @PostMapping("/registered/addedReview")
    public String addedReview(@ModelAttribute("movie") Movie movie,
                              @ModelAttribute("review") Review review,
                              @RequestParam("rating") int rating,
                              Model model) {
        if (reviewRepository.existsByTitle(review)) {
            model.addAttribute("messaggioErrore", "Questa recensione ha un titolo che esiste già :(");
            return "registered/formAddReview.html";
        } else {
            review.setReviewedMovie(movie);
            review.setRating(rating);
            review.setAuthor(userService.getCurrentUser());

            this.reviewRepository.save(review);
            movie.getReviews().add(review);

            model.addAttribute("movie", movie);
            return "movie.html";
        }
    }
}
