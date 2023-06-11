package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Set<Artist> findAllByMoviesActedInIsNotContaining(Movie movie);
    Set<Artist> findAllByMoviesActedInIsContaining(Movie movie);
    boolean existsByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth);
}