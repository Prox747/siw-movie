package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.util.ModelPreparationUtil;
import it.uniroma3.siw.validator.ArtistValidator;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ArtistController {
    @Autowired
    ArtistService artistService;
    @Autowired
    ArtistValidator artistValidator;
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
    public String addArtist(@RequestParam String dateOfBirthString,
                            @RequestParam String dateOfDeathString,
                            @RequestParam("image") MultipartFile multipartFile,
                            @Valid @ModelAttribute("artist") Artist artist,
                            BindingResult bindingResult,
                            Model model) {

        //va prima assegnata la data di nascita, altrimenti non si può validare
        artistService.setDateOfBirth(artist, dateOfBirthString);

        artistValidator.validate(artist, bindingResult);
        if (!multipartFile.isEmpty() && !bindingResult.hasErrors()) {
            try {
                artistService.setImageForArtist(artist, multipartFile);
            } catch (IOException e) {
                model.addAttribute("erroreUpload", "Errore nel caricamento dell'immagine");
                return "admin/formAddArtist";
            }

            if (!dateOfDeathString.isEmpty()) {
                artistService.setDateOfDeath(artist, dateOfDeathString);
            }

            this.artistService.save(artist);
            return modelPreparationUtil.prepareModelForArtistTemplate(
                    "artist.html",
                            model,
                            artist);
        } else {
            return "admin/formAddArtist";
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
