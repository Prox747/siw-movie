package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    public boolean existsByTitleAndReviewedMovie(String title, Movie movie);
}
