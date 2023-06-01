package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The UserService handles logic for Users.
 */
@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    CredentialsService credentialsService;

    /**
     * This method retrieves a User from the DB based on its ID.
     * @param id the id of the User to retrieve from the DB
     * @return the retrieved User, or null if no User with the passed ID could be found in the DB
     */
    @Transactional
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    //returs: null if there is no logged user
    @Transactional
    public User getCurrentUser() {
        Optional<Credentials> currentCredentials = credentialsService.getCurrentCredentials();
        if(currentCredentials.isPresent())
            return currentCredentials.get().getUser();
        else return null;
    }

    /**
     * This method saves a User in the DB.
     * @param user the User to save into the DB
     * @return the saved User
     * @throws DataIntegrityViolationException if a User with the same username
     *                              as the passed User already exists in the DB
     */
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * This method retrieves all Users from the DB.
     * @return a List with all the retrieved Users
     */
    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }

    public void addImageToUser(User user, MultipartFile multipartFile) throws IOException {
        //questa linea è necessaria per evitare attacchi di iniezione di codice attraverso il nome del file
        // (possono inserire un nome di file che contiene un path e quindi accedere a file che non dovrebbero o cose simili supercattive)
        String fileName = user.getUsername() + "_profile_pic.jpg";
        user.setImageFileName(fileName);
        String uploadDir = "src/main/upload/images/userProfilePics/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }
}
