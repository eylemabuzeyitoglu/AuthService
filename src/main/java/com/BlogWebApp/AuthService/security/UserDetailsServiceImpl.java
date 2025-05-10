package com.BlogWebApp.AuthService.security;


import com.BlogWebApp.Common.client.UserServiceClient;
import com.BlogWebApp.Common.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserResponse userResponse = userServiceClient.getUserByEmail(email);

        if (userResponse == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userResponse.getEmail(),
                userResponse.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(userResponse.getRole().name()))
        );
    }
}
