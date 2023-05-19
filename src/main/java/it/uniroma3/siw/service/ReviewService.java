package it.uniroma3.siw.service;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MovieService movieService;
    @Autowired CredentialsService credentialsService;

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

    public void initializeAndSaveReview(int rating, Long movieId, Review review) {
        Movie movie = this.movieService.findById(movieId);
        Credentials authorCredentials = this.credentialsService.getCurrentCredentials();

        review.setCreationDate(LocalDate.now());
        review.setReviewedMovie(movie);
        review.setRating(rating);
        review.setAuthor(authorCredentials);

        //ATTENTO A SALVARE PRIMA LA REVIEW E POI A SALVARE IL MOVIE PER UPDATARLO,
        // SENNO SE INVERTI CREA LA REVIEW DUE VOLTE PERCHE' IL MOVIE HA CASCADE ALL
        this.save(review);

        authorCredentials.setReview(review);
        movie.getReviews().add(review);

        movieService.save(movie);

        this.credentialsService.saveCredentials(authorCredentials);
    }
}
