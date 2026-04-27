package com.airline.service.impl;

import com.airline.config.JwtProvider;
import com.airline.enums.UserRole;
import com.airline.mapper.UserMapper;
import com.airline.model.User;
import com.airline.payload.dto.UserDTO;
import com.airline.payload.response.AuthResponse;
import com.airline.repository.UserRepository;
import com.airline.service.AuthService;
import com.airline.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public AuthResponse signup(UserDTO req) throws Exception {
        User existingUser = userRepository.findByEmail(req.getEmail());
        if(existingUser != null){
            throw new Exception("Email already registered");
        }
        if(req.getRole() == UserRole.ROLE_SYSTEM_ADMIN){
            throw new Exception("Cannot register as system admin");
        }

        User newUser = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .role(req.getRole())
                .fullName(req.getFullName())
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken
                (savedUser.getEmail(), savedUser.getPassword());

        String jwt = jwtProvider.generateToken(authentication, savedUser.getId());

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setUser(UserMapper.toDTO(savedUser));
        response.setTitle("Welcome "+savedUser.getFullName());
        response.setMessage("Registered Successfully!");

        return response;
    }

    @Override
    public AuthResponse login(String email, String password) throws Exception {
        Authentication authentication = authenticate(email, password);

        User user = userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtProvider.generateToken(authentication, user.getId());

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setUser(UserMapper.toDTO(user));
        response.setTitle("Welcome back "+user.getFullName());
        response.setMessage("Login Successfully!");

        return response;
    }

    private Authentication authenticate(String email,  String password) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new Exception("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }

}
