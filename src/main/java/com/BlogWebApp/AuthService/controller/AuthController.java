package com.BlogWebApp.AuthService.controller;

import com.BlogWebApp.AuthService.dto.AuthResponse;
import com.BlogWebApp.AuthService.dto.LoginRequest;
import com.BlogWebApp.AuthService.dto.RegisterRequest;
import com.BlogWebApp.AuthService.dto.UserResponse;
import com.BlogWebApp.AuthService.model.User;
import com.BlogWebApp.AuthService.repository.UserRepository;
import com.BlogWebApp.AuthService.security.JwtUtil;
import com.BlogWebApp.AuthService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        UserResponse response = new UserResponse(user.getUserId(), user.getUserName(), user.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            // "Bearer " varsa onu sil
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            jwtUtil.validateToken(token);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }
}
