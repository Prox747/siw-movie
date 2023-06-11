package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MovieValidator implements Validator {
    @Autowired
    MovieRepository movieRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Movie.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Movie movie = (Movie)o;
        if(movie.getTitle()!=null && movie.getYear()!=null &&
                   movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())) {
            errors.reject("movie.duplicate");
        }
    }
}
