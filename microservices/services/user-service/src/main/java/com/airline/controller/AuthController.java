package com.airline.controller;

import com.airline.payload.dto.UserDTO;
import com.airline.payload.request.LoginRequest;
import com.airline.payload.response.AuthResponse;
import com.airline.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup( @RequestBody @Valid UserDTO userDTO) throws Exception {
        AuthResponse authResponse = authService.signup(userDTO);
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) throws Exception {
        AuthResponse response  = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(response);
    }

}
