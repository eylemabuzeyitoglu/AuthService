package com.BlogWebApp.AuthService.service;


import com.BlogWebApp.AuthService.security.JwtUtil;
import com.BlogWebApp.CommonSecurity.client.UserServiceClient;
import com.BlogWebApp.CommonSecurity.dto.AuthResponse;
import com.BlogWebApp.CommonSecurity.dto.LoginRequest;
import com.BlogWebApp.CommonSecurity.dto.RegisterRequest;
import com.BlogWebApp.CommonSecurity.dto.UserResponse;
import com.BlogWebApp.CommonSecurity.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceClient userServiceClient;

    // Register method
    public AuthResponse register(RegisterRequest request) {
        // Şifreyi encode ediyoruz
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Burada User entity'si yerine UserResponse DTO kullanacağız
        // Bu UserResponse, DB'den çekilen bir User objesini temsil eder
        UserResponse userResponse = new UserResponse();
        userResponse.setUserName(request.getUserName());
        userResponse.setEmail(request.getEmail());
        userResponse.setPassword(encodedPassword);
        userResponse.setRole(Role.USER);  // UserRole enum

        // Burada sadece token oluşturuluyor, User entity'si yerine UserResponse kullanıyoruz
        String token = jwtUtil.generateToken(userResponse.getUserId(), userResponse.getRole().name());
        return new AuthResponse(token);
    }

    // Login method
    public AuthResponse login(LoginRequest request) {
        // Authentication işlemi
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        UserResponse userResponse = userServiceClient.getUserById(request.getUserId());

        // Token oluşturuluyor
        String token = jwtUtil.generateToken(userResponse.getUserId(), userResponse.getRole().name());
        return new AuthResponse(token);
    }

    // Kullanıcıyı başka bir servisten almak için kullanılan metot
    public UserResponse getUserFromUserService(Long id) {
        return userServiceClient.getUserById(id);
    }
}