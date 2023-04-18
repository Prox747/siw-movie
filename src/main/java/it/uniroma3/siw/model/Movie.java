package it.uniroma3.siw.model;

import jakarta.persistence.*;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Integer year;
    private String urlImage;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="artist_id_director")
    private Artist director;
    @OneToMany
    @JoinColumn(name = "movie_id")
    private List<News> news;
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Artist> actors;

    public Movie() {
        this.news = new ArrayList<News>();
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
