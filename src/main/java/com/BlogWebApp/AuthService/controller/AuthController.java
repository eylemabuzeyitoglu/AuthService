package com.BlogWebApp.AuthService.controller;


import com.BlogWebApp.AuthService.security.JwtUtil;
import com.BlogWebApp.AuthService.service.AuthService;
import com.BlogWebApp.CommonSecurity.client.UserServiceClient;
import com.BlogWebApp.CommonSecurity.dto.AuthResponse;
import com.BlogWebApp.CommonSecurity.dto.LoginRequest;
import com.BlogWebApp.CommonSecurity.dto.RegisterRequest;
import com.BlogWebApp.CommonSecurity.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userServiceClient.getUserById(id);
        return ResponseEntity.ok(userResponse);
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