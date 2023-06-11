package it.uniroma3.siw.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String nationality;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private String imageFileName;
    @OneToMany(mappedBy = "director")
    private List<Movie> directedMovies;
    @ManyToMany(mappedBy = "actors")
    private List<Movie> moviesActedIn;

    public Artist() {
        this.directedMovies = new ArrayList<Movie>();
        this.moviesActedIn = new ArrayList<Movie>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(name, artist.name) && Objects.equals(surname, artist.surname) && Objects.equals(nationality, artist.nationality) && Objects.equals(dateOfBirth, artist.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, nationality, dateOfBirth);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Movie> getDirectedMovies() {
        return directedMovies;
    }

    public void setDirectedMovies(List<Movie> directedMovies) {
        this.directedMovies = directedMovies;
    }

    public List<Movie> getMoviesActedIn() {
        return moviesActedIn;
    }

    public void setMoviesActedIn(List<Movie> moviesActedIn) {
        this.moviesActedIn = moviesActedIn;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

