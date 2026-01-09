package com.benconsult.user_management_api.users.controller;

import com.benconsult.user_management_api.users.dto.auth.AuthRequestDTO;
import com.benconsult.user_management_api.users.dto.auth.AuthResponseDTO;
import com.benconsult.user_management_api.users.service.JwtUtilService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtilService jwtUtilService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO authRequestDto) {

        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.username(),
                        authRequestDto.password()
                )
        );

        // Cargar UserDetails
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        authRequestDto.username()
                );

        // Generar tokens
        String token = jwtUtilService.generateToken(userDetails);
        String refreshToken =
                jwtUtilService.generateRefreshToken(userDetails);

        // Responder
        return ResponseEntity.ok(
                new AuthResponseDTO(token, refreshToken)
        );
    }
}

