package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.util.ModelPreparationUtil;
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
    @Autowired
    MovieService movieService;
    @Autowired
    ModelPreparationUtil modelPreparationUtil;

    @GetMapping("/artists/{id}")
    public String getActor(@PathVariable("id") Long id, Model model) throws NotFoundException {
        return modelPreparationUtil.prepareModelForArtistTemplate(
                "artist.html",
                        model,
                        this.artistService.findById(id));
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
            return modelPreparationUtil.prepareModelForArtistTemplate(
                    "artist.html",
                    model,
                    artist);
        } else {
            model.addAttribute("messaggioErrore", "Questo artista esiste già");
            return "formAddArtist.html";
        }
    }

    @GetMapping("/admin/deleteArtist/{artistId}")
    public String deleteMovie(@PathVariable("artistId") Long artistId, Model model) {
        artistService.deleteArtist(artistId);
        return modelPreparationUtil.prepareModelForIndexTemplate(
                "index.html",
                model,
                artistService.findAll(),
                movieService.findAllByOrderByYearDesc());
    }
}
