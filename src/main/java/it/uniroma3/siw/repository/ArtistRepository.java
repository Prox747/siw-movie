package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    public List<Artist> findAllByMoviesActedInIsNotContaining(Movie movie);

    public List<Artist> findAllByMoviesActedInIsContaining(Movie movie);
}