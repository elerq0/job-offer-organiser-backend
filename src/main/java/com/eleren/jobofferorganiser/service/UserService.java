package com.eleren.jobofferorganiser.service;

import com.eleren.jobofferorganiser.config.JwtService;
import com.eleren.jobofferorganiser.model.User;
import com.eleren.jobofferorganiser.repository.RoleRepository;
import com.eleren.jobofferorganiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityExistsException("User [" + username + "] do not exist in the database"));
    }

    public void create(String username, String password, String email) throws Exception {
        if(userRepository.findByUsername(username).isPresent())
            throw new Exception(String.format("User [%1$s] already exists", username));

        if(userRepository.findByEmail(email).isPresent())
            throw new Exception(String.format("Email [%1$s] is already taken", email));

        User user = new User(username, passwordEncoder.encode(password), email);
        user.addRole(roleRepository.findByName("USER")
                .orElseThrow(() -> new Exception("Role [USER] does not exist in the database")));

        userRepository.save(user);
    }

    public String login(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User [" + username + "] does not exist in the database"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new Exception("Passwords do not match!");

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return JwtService.generateToken(username, null);
    }

    public User getByUsername(String username) throws Exception {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User [" + username + "] does not exist in the database"));
    }
}