package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping("/artists")
    public String showAllActors(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        return "artists.html";
    }

    @GetMapping("/artist/{id}")
    public String getActor(@PathVariable("id") Long id, Model model) {
        model.addAttribute("artist", this.artistRepository.findById(id).get());
        return "artist.html";
    }

    @GetMapping("/admin/formAddArtist")
    public String formAddArtist(Model model){
        model.addAttribute("artist", new Artist());
        return "admin/formAddArtist.html";
    }

    @PostMapping("/admin/addedArtist")
    public String addArtist(@ModelAttribute("artist") Artist artist, Model model) {
        if (!artistRepository.existsArtistByNameAndSurnameAndDateOfBirth(artist.getName(), artist.getSurname(), artist.getDateOfBirth())) {
            this.artistRepository.save(artist);
            model.addAttribute("artist", artist);
            return "artist.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo artista esiste già");
            return "formAddArtist.html";
        }
    }
}
