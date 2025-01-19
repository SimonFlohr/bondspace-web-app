package com.bondspace.service;

import com.bondspace.domain.dto.LoginRequestDTO;
import com.bondspace.domain.dto.RegistrationRequestDTO;
import com.bondspace.domain.model.User;
import com.bondspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(RegistrationRequestDTO request) {
        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new IllegalArgumentException("Email address is already in use.");
        }

        User newUser = new User();
        newUser.setEmailAddress(request.getEmailAddress());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        userRepository.save(newUser);
        return "User registered successfully.";
    }

    public String loginUser(LoginRequestDTO request) {
        User user = userRepository.findByEmailAddress(request.getEmailAddress())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password.");
        }

        return "Login successful.";
    }

}
