package it.uniroma3.siw.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private int rating;
    private LocalDate creationDate;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie reviewedMovie;
    @OneToOne
    @JoinColumn(name = "authorCredentials_id")
    private User author;

    public Review() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && Objects.equals(id, review.id) && Objects.equals(title, review.title) && Objects.equals(content, review.content) && Objects.equals(reviewedMovie, review.reviewedMovie) && Objects.equals(author, review.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, rating, reviewedMovie, author);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie getReviewedMovie() {
        return reviewedMovie;
    }

    public void setReviewedMovie(Movie reviewedMovie) {
        this.reviewedMovie = reviewedMovie;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}

