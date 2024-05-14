package org.cce.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.Token;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Role;
import org.cce.backend.exception.UserAlreadyExistsException;
import org.cce.backend.mapper.RegisterRequestDTOUserMapper;
import org.cce.backend.repository.TokenRepository;
import org.cce.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RegisterRequestDTOUserMapper registerRequestDTOUserMapper;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        User user = registerRequestDTOUserMapper.RegisterRequestDTOToUser(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        validateUserNotExists(request);
        System.out.println(user);
        userRepository.save(user);
        return generateJwt(user);
    }

    private void validateUserNotExists(RegisterRequestDTO request) {
        userRepository.findByUsername(request.getUsername())
                .stream()
                .findFirst()
                .ifPresent((item) -> {
                    throw new UserAlreadyExistsException("User with username " + item.getUsername() + " already exists");
                });

        userRepository.findByEmail(request.getEmail())
                .stream()
                .findFirst()
                .ifPresent((item) -> {
                    throw new UserAlreadyExistsException("User with email " + item.getEmail() + " already exists");
                });
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String username = request.getUsername();
        User user = userRepository.findByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        return generateJwt(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String jwt;
        if(header == null ||!header.startsWith("Bearer ")){
            return;
        }
        final int tokenStart = "Bearer ".length();
        jwt = header.substring(tokenStart);
        disableJwt(jwt);
    }

    private void disableJwt(String jwtToken){
        List<Token> token=tokenRepository.findByTokenKey(jwtToken);
        token.stream().findFirst().ifPresent((t)->{
            t.setIsValid(false);
            tokenRepository.save(t);
        });
    }

    private AuthenticationResponseDTO generateJwt(User user) {
        String jwtToken = jwtService.generateToken(user);
        Token token = Token.builder()
                .tokenKey(jwtToken)
                .user(user)
                .isValid(true)
                .build();
        System.out.println(token);
        tokenRepository.save(token);
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

}