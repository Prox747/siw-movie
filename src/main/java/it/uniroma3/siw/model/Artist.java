package it.uniroma3.siw.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String nationality;
    private LocalTime dateOfBirth;
    @OneToMany(mappedBy = "director")
    private List<Movie> directedMovies;
    @ManyToMany(mappedBy = "actors")
    private List<Movie> moviesActedIn;

    public Artist() {
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

    public LocalTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}

