package com.BlogWebApp.AuthService.service;


import com.BlogWebApp.AuthService.security.JwtUtil;
import com.BlogWebApp.Common.client.UserServiceClient;
import com.BlogWebApp.Common.dto.request.LoginRequest;
import com.BlogWebApp.Common.dto.request.RegisterRequest;
import com.BlogWebApp.Common.dto.response.AuthResponse;
import com.BlogWebApp.Common.dto.response.UserResponse;
import com.BlogWebApp.Common.model.Role;
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

    public AuthResponse register(RegisterRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserResponse userResponse = new UserResponse();
        userResponse.setUserName(request.getUserName());
        userResponse.setEmail(request.getEmail());
        userResponse.setPassword(encodedPassword);
        userResponse.setRole(Role.USER);  // UserRole enum

        String token = jwtUtil.generateToken(userResponse.getUserId(), userResponse.getRole().name());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        UserResponse userResponse = userServiceClient.getUserById(request.getUserId());

        String token = jwtUtil.generateToken(userResponse.getUserId(), userResponse.getRole().name());
        return new AuthResponse(token);
    }

    public UserResponse getUserFromUserService(Long id) {
        return userServiceClient.getUserById(id);
    }
}