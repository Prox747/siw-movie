package it.uniroma3.siw.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @Min(1900)
    @Max(2023)
    private Integer year;
    private String urlImage;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="artist_id_director")
    private Artist director;
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Artist> actors;

    public Movie() {
        this.actors = new ArrayList<Artist>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title) && Objects.equals(year, movie.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artist getDirector() {
        return director;
    }

    public void setDirector(Artist director) {
        this.director = director;
        director.getDirectedMovies().add(this);
    }

    public List<Artist> getActors() {return actors;}

    public void setActors(List<Artist> actors) {
        this.actors = actors;
    }

    public void addActor(Artist actor) {
        this.actors.add(actor);
        actor.getMoviesActedIn().add(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
