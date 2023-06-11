package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ArtistValidator implements Validator {
    @Autowired
    ArtistRepository artistRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Artist.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Artist artist = (Artist) o;
        if(     artist.getName()!=null && artist.getSurname()!=null &&
                artist.getDateOfBirth()!=null &&
                artistRepository.existsByNameAndSurnameAndDateOfBirth(
                        artist.getName(), artist.getSurname(), artist.getDateOfBirth()) ) {
            errors.reject("artist.duplicate");
        }
    }
}
