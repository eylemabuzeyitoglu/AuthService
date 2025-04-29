package com.BlogWebApp.AuthService.service;

import com.BlogWebApp.AuthService.dto.AuthResponse;
import com.BlogWebApp.AuthService.dto.LoginRequest;
import com.BlogWebApp.AuthService.dto.RegisterRequest;
import com.BlogWebApp.AuthService.model.Role;
import com.BlogWebApp.AuthService.model.User;
import com.BlogWebApp.AuthService.repository.UserRepository;
import com.BlogWebApp.AuthService.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Bu email kullanılıyor!");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(encodedPassword)  // Şifreyi şifrele
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUserId(), user.getRole().name());
        return new AuthResponse(token);
    }


    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));


        String token = jwtUtil.generateToken(user.getUserId(), user.getRole().name());
        return new AuthResponse(token);
    }
}
