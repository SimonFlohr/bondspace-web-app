package com.bondspace.controller;

import com.bondspace.domain.dto.LoginRequestDTO;
import com.bondspace.domain.dto.RegistrationRequestDTO;
import com.bondspace.domain.model.User;
import com.bondspace.repository.UserRepository;
import com.bondspace.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDTO request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request) {
        String response = authService.loginUser(request);
        System.out.println("Session ID: " + httpSession.getId());
        System.out.println("User ID in session: " + httpSession.getAttribute("userId"));
        return ResponseEntity.ok(Map.of("message", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        String response = authService.logoutUser();
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/status")
    public ResponseEntity<Void> checkAuthStatus() {
        System.out.println("Checking status - Session ID: " + httpSession.getId());
        System.out.println("User ID in status check: " + httpSession.getAttribute("userId"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, String>> getCurrentUser() {
        Integer userId = (Integer) httpSession.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(Map.of("firstName", user.getFirstName()));
    }

}
