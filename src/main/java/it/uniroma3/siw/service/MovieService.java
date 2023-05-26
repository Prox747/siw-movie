package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MovieService {

    @Autowired MovieRepository movieRepository;

    public Movie findById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> findAll() {
        List<Movie> result = new ArrayList<>();
        Iterable<Movie> iterable = this.movieRepository.findAll();
        for(Movie movie : iterable)
            result.add(movie);
        return result;
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

    public List<Movie> findAllByOrderByYearDesc() {
        return movieRepository.findAllByOrderByYearDesc();
    }

    public List<Movie> findByYear(Integer year) {
        return movieRepository.findByYear(year);
    }

    public void addImageToMovie(Movie movie, MultipartFile multipartFile) throws IOException {
        //questa linea è necessaria per evitare attacchi di iniezione di codice attraverso il nome del file
        // (possono inserire un nome di file che contiene un path e quindi accedere a file che non dovrebbero o cose simili supercattive)
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        movie.setImageFileName(fileName);
        String uploadDir = "src/main/upload/images/moviesImages/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    public void deleteMovie(Long movieId) {
        Movie movieToDelete = movieRepository.findById(movieId).get();
        wipeFields(movieToDelete);
        movieRepository.delete(movieToDelete);
    }

    private void wipeFields(Movie movieToDelete) {
        //in teoria nulla referenzia più le recensioni così
        for (Review r: movieToDelete.getReviews()
             ) {
            r.getAuthor().setReview(null);
        }
        //aggiornaiamo le liste dentro artist
        for (Artist a: movieToDelete.getActors()
        ) {
            a.getMoviesActedIn().remove(movieToDelete);
            a.getDirectedMovies().remove(movieToDelete);
        }
    }
}
