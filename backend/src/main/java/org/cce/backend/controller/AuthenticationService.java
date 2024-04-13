package org.cce.backend.controller;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Role;
import org.cce.backend.repository.UserRepository;
import org.cce.backend.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        User user = User
                .builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }
}
