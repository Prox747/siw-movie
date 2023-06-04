package it.uniroma3.siw.util;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//semplice classe di utilità per salvare un file in una directory
public class FileUploadUtil {
    UserService userService;

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void saveCurrentUserProfilePicForHeader(User currentUser) {
        String defaultProfilePicPath = "src/main/upload/images/userProfilePics/default_pic.jpg";
        String currentProfilePicPath = "src/main/upload/images/userProfilePics/current_user_pic.jpg";
        String userProfilePicPath;
        Path sourcePath;

        try {
            //controlla se esiste l'immagine del profilo dell'utente, altrimenti usa quella di default
            if(currentUser.getImageFileName() != null) {
                userProfilePicPath = "src/main/upload/images/userProfilePics/" + currentUser.getImageFileName();
            } else {
                userProfilePicPath = defaultProfilePicPath;
            }

            sourcePath = Files.exists(Paths.get(userProfilePicPath)) ? Paths.get(userProfilePicPath) : Paths.get(defaultProfilePicPath);
            Path targetPath = Paths.get(currentProfilePicPath);

            //copia l'immagine del profilo dell'utente nel file che viene usato per mostrare l'immagine del profilo dell'utente nell'header
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Current user image in header has been updated.");
        } catch (IOException e) {
            System.err.format("Error updating current user image in header: %s%n", e);
        }
    }
}
