package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public Optional<Credentials> getCurrentCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = this.getCredentials(userDetails.getUsername());
            return Optional.of(credentials);
        }
    }

    @Transactional
    public boolean userIsAdmin() {
        Optional<Credentials> currentCredentials = this.getCurrentCredentials();
        if(currentCredentials.isPresent()) {
            if(currentCredentials.get().getRuolo().equals(Credentials.ADMIN_ROLE)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean userIsRegistered() {
        Optional<Credentials> currentCredentials = this.getCurrentCredentials();
        if(currentCredentials.isPresent()) {
            if(currentCredentials.get().getRuolo().equals(Credentials.DEFAULT_ROLE)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRuolo(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
}
