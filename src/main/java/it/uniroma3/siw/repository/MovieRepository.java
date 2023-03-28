package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface MovieRepository extends CrudRepository<Movie, Long> {
    public List<Movie> findByYear(Integer year);
    public boolean existsByTitleAndYear(String title, Integer year);
}
