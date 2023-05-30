package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.util.FileUploadUtil;
import it.uniroma3.siw.util.ModelPreparationUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ArtistService {
    @Autowired
    ArtistRepository artistRepository;

    public List<Artist> findAll() {
        List<Artist> result = new ArrayList<>();
        Iterable<Artist> iterable = this.artistRepository.findAll();
        for(Artist artist : iterable)
            result.add(artist);
        return result;
    }

    public Artist findById(Long id) throws NotFoundException {
        return artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artist not found with id: " + id));
    }

    public Set<Artist> actorsNotInMovie(Movie movie) {
        return artistRepository.findAllByMoviesActedInIsNotContaining(movie);
    }

    public Set<Artist> actorsForMovie(Movie movie) {
        return artistRepository.findAllByMoviesActedInIsContaining(movie);
    }

    public boolean existsArtistByNameAndSurnameAndDateOfBirth(String name, String surname, LocalDate dateOfBirth) {
        return artistRepository.existsArtistByNameAndSurnameAndDateOfBirth(name, surname, dateOfBirth);
    }

    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    public void delete(Artist artist) {
        artistRepository.delete(artist);
    }

    public void setImageForArtist(Artist artist, MultipartFile multipartFile) throws IOException {
        //questa linea è necessaria per evitare attacchi di iniezione di codice attraverso il nome del file
        // (possono inserire un nome di file che contiene un path e quindi accedere a file che non dovrebbero o cose simili supercattive)
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        artist.setImageFileName(fileName);
        String uploadDir = "src/main/upload/images/artistsImages/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    public void setDateOfBirth(Artist artist, String dateOfBirthString) {
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the string into a LocalDate object
        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString, formatter);
        artist.setDateOfBirth(dateOfBirth);
    }

    public void setDateOfDeath(Artist artist, String dateOfDeathString) {
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the string into a LocalDate object
        LocalDate dateOfDeath = LocalDate.parse(dateOfDeathString, formatter);
        artist.setDateOfDeath(dateOfDeath);
    }

    public void deleteArtist(Long artistId) {
        Artist artistToDelete = artistRepository.findById(artistId).get();
        wipeReferences(artistToDelete);
        artistRepository.delete(artistToDelete);
    }

    private void wipeReferences(Artist artistToDelete) {
        //aggiorniamo i direttori dei movies
        for (Movie m: artistToDelete.getDirectedMovies()
        ) {
            m.setDirector(null);
        }
        //aggiornaiamo la lista dentro movie
        for (Movie m: artistToDelete.getMoviesActedIn()
        ) {
            m.getActors().remove(artistToDelete);
        }
    }
}
