package it.uniroma3.siw.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String name;
    String surname;
    @Email(message = "Email not valid")
    String email;

    //i need it for easy use of review objects
    String username;

    //FORSE QUA IL CASCADE ALL NON SERVE, ATTENTO QUANDO CANCELLI UNA REVIEW
    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "author")
    private Review review;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review r) {
        this.review = r;
    }
}
