package com.bondspace.service;

import com.bondspace.domain.dto.LoginRequestDTO;
import com.bondspace.domain.dto.RegistrationRequestDTO;
import com.bondspace.domain.model.User;
import com.bondspace.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession; // Add HttpSession

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
                .orElseThrow(() -> new IllegalArgumentException("Email address not found."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password.");
        }

        // Set session attributes
        httpSession.setAttribute("userId", user.getId());
        httpSession.setAttribute("email", user.getEmailAddress());

        return "Login successful.";
    }

    public String logoutUser() {
        httpSession.invalidate(); // Clear the http session
        return "Logged out successfully.";
    }

}
