package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ArtistController {
    @Autowired
    ArtistService artistService;

    @GetMapping("/artists")
    public String showAllActors(Model model) {
        model.addAttribute("artists", artistService.findAll());
        return "artists.html";
    }

    @GetMapping("/artist/{id}")
    public String getActor(@PathVariable("id") Long id, Model model) throws NotFoundException {
        model.addAttribute("artist", this.artistService.findById(id));
        return "artist.html";
    }

    @GetMapping("/admin/formAddArtist")
    public String formAddArtist(Model model){
        model.addAttribute("artist", new Artist());
        return "admin/formAddArtist.html";
    }

    @PostMapping("/admin/addedArtist")
    public String addArtist(@ModelAttribute("artist") Artist artist, @RequestParam String dateOfBirthString,
                            @RequestParam String dateOfDeathString,
                            @RequestParam("image") MultipartFile multipartFile,
                            Model model) throws IOException {
        artistService.setImageForArtist(artist, multipartFile);
        artistService.setDateOfBirth(artist, dateOfBirthString);

        if (!dateOfDeathString.isEmpty()) {
            artistService.setDateOfDeath(artist, dateOfDeathString);
        }

        if (!artistService.existsArtistByNameAndSurnameAndDateOfBirth(artist.getName(), artist.getSurname(), artist.getDateOfBirth())) {
            this.artistService.save(artist);
            model.addAttribute("artist", artist);
            return "artist.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo artista esiste già");
            return "formAddArtist.html";
        }
    }
}
