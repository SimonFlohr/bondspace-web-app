package com.bondspace.controller;

import com.bondspace.domain.dto.LoginRequestDTO;
import com.bondspace.domain.dto.RegistrationRequestDTO;
import com.bondspace.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequestDTO request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        String response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String response = authService.logoutUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Void> checkAuthStatus() {
        // If the session is not valid, the SessionValidationFilter will return 401
        // If we get here, it means the session is valid
        return ResponseEntity.ok().build();
    }

}
