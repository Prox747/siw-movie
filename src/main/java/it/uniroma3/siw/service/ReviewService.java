package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MovieService movieService;
    @Autowired UserService userService;

    public boolean existsByTitle(String title) {
        return reviewRepository.existsByTitle(title);
    }

    public List<Review> findAll() {
        List<Review> result = new ArrayList<>();
        Iterable<Review> iterable = this.reviewRepository.findAll();
        for(Review review : iterable)
            result.add(review);
        return result;
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    public void initializeReview(int rating, Long movieId, Review review) {
        Movie movie = this.movieService.findById(movieId);
        User user = userService.getCurrentUser();

        review.setReviewedMovie(movie);
        review.setRating(rating);
        review.setAuthor(user);

        user.setReview(review);
        movie.getReviews().add(review);

        movieService.save(movie);
        //non va fatto perchè c'è già il cascade
        //this.save(review);
        this.userService.saveUser(user);
    }
}
