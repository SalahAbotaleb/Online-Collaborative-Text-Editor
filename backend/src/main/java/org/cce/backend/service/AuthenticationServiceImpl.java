package org.cce.backend.service;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Role;
import org.cce.backend.mapper.RegisterRequestDTOUserMapper;
import org.cce.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RegisterRequestDTOUserMapper registerRequestDTOUserMapper;
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        User user = registerRequestDTOUserMapper.RegisterRequestDTOToUser(request);
        user.setRole(Role.USER);
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
        String username = request.getUsername();
        UserDetails user = userRepository.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(()->new UsernameNotFoundException("User with username "+username+" not found"));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }
}
