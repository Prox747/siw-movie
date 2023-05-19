package it.uniroma3.siw.model;

import javax.persistence.*;

@Entity
public class Credentials {

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String DEFAULT_ROLE = "DEFAULT";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String username;
    String password;
    String ruolo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    User user;

    //FORSE QUA IL CASCADE ALL NON SERVE, ATTENTO QUANDO CANCELLI UNA REVIEW
    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "author")
    private Review review;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
