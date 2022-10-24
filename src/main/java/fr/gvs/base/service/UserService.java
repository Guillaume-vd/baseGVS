package fr.gvs.base.service;

import java.util.List;
import java.util.Optional;

import fr.gvs.base.exception.Http400BadRequestException;
import fr.gvs.base.exception.Http401UnauthorizedException;
import fr.gvs.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.gvs.base.exception.Http403ForbiddenException;
import fr.gvs.base.model.User;
import fr.gvs.base.security.Role;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user, String raw_password) throws Http400BadRequestException {
        if(userRepository.findOne(Example.of(new User().setEmail(user.getEmail()))).isPresent()) {
            throw new Http400BadRequestException("Un utilisateur avec cette adresse email existe déjà.");
        }

        user.setPassword(passwordEncoder.encode(raw_password));
        user.setEnabled(true);

        return userRepository.saveAndFlush(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findOne(Example.of(new User().setEmail(email)));
    }

    public boolean isAuthenticated() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> auth.isAuthenticated())
                .orElse(false);
    }

    public void assertAuthenticated() throws Http403ForbiddenException {
        if(!this.isAuthenticated()) throw new Http403ForbiddenException("Aucun utilisateur connecté.");
    }

    public void assertRole(Role role) throws Http403ForbiddenException, Http401UnauthorizedException {
        Role userRole = getAuthenticatedUser().map(user -> user.getRole()).orElseThrow(() -> new Http403ForbiddenException("Aucun utilisateur connecté."));
        
        // if needed role is more than user role
        if(role.compareTo(userRole) > 0)
        {
            throw new Http401UnauthorizedException("Opération interdite.");
        }
    }

    public Optional<User> getAuthenticatedUser() {
        if(!this.isAuthenticated()) return Optional.ofNullable(null);

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> auth.getPrincipal() instanceof UserDetails ? auth : null)
                .map(auth -> (UserDetails) auth.getPrincipal())
                .map(userDetails -> userDetails.getUsername())
                .flatMap(this::findUserByEmail);
    }

    public long countUserWithRole(Role role) {
        return userRepository.count(Example.of(new User().setRole(role).setEnabled(true)));
    }

    public List<User> listAllUser() {
        return userRepository.findAll();
    }

    public void modifyUser(User user) {
        userRepository.saveAndFlush(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void changePassword(User user, String raw_password) {
        user.setPassword(passwordEncoder.encode(raw_password));
        userRepository.saveAndFlush(user);
    }
}
