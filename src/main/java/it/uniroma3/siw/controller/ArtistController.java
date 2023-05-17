package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    public String addArtist(@ModelAttribute("artist") Artist artist, @RequestParam String dateOfBirthString,
                            @RequestParam String dateOfDeathString,
                            @RequestParam("image") MultipartFile multipartFile,
                            Model model) throws IOException {
        // Define the desired date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the string into a LocalDate object
        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthString, formatter);
        artist.setDateOfBirth(dateOfBirth);

        //questa linea è necessaria per evitare attacchi di iniezione di codice attraverso il nome del file
        // (possono inserire un nome di file che contiene un path e quindi accedere a file che non dovrebbero o cose simili supercattive)
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        artist.setImageFileName(fileName);
        String uploadDir = "src/main/upload/images/artistsImages/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        if (!dateOfDeathString.isEmpty()) {
            LocalDate dateOfDeath = LocalDate.parse(dateOfDeathString, formatter);
            artist.setDateOfDeath(dateOfDeath);
        }

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
