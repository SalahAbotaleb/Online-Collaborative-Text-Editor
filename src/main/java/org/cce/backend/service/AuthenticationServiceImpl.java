package org.cce.backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.Token;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Role;
import org.cce.backend.exception.UserAlreadyExistsException;
import org.cce.backend.mapper.AuthenticationDTOUserMapper;
import org.cce.backend.mapper.RegisterRequestDTOUserMapper;
import org.cce.backend.repository.TokenRepository;
import org.cce.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final AuthenticationDTOUserMapper authenticationDTOUserMapper;
    private final UserDetailsService userDetailsService;
    private final EntityManager entityManager;

    @Transactional
    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        User user = registerRequestDTOUserMapper.RegisterRequestDTOToUser(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        validateUserNotExists(request);
        System.out.println(user);
        try {
            entityManager.persist(user);
            entityManager.flush();
        } catch (PersistenceException ex) {
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " or email " + user.getEmail() + " already exists");
        }
        // return empty token
//        return AuthenticationResponseDTO.builder().token("").build();
        return generateJwt(user);
    }

//    private void validateUserNotExists(RegisterRequestDTO request) {
//        userRepository.findByUsername(request.getUsername())
//                .stream()
//                .findFirst()
//                .ifPresent((item) -> {
//                    throw new UserAlreadyExistsException("User with username " + item.getUsername() + " already exists");
//                });
//
//        userRepository.findByEmail(request.getEmail())
//                .ifPresent((item) -> {
//                    throw new UserAlreadyExistsException("User with email " + item.getEmail() + " already exists");
//                });
//    }

    @Transactional
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        System.out.println("test");
        User user = authenticationDTOUserMapper.AuthenticationRequestDTOToUser(request);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        String username = request.getUsername();
//        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        System.out.println("test1");
        System.out.println("test2");
        return generateJwt(user);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String jwt;
        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        final int tokenStart = "Bearer ".length();
        jwt = header.substring(tokenStart);
        disableJwt(jwt);
    }

    private void disableJwt(String jwtToken) {
        List<Token> token = tokenRepository.findByTokenKey(jwtToken);
        token.stream().findFirst().ifPresent((t) -> {
            t.setIsValid(false);
            tokenRepository.save(t);
        });
    }

    @Transactional
    protected AuthenticationResponseDTO generateJwt(User user) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        String jwtToken = jwtService.generateToken(userDetails);
        Token token = Token.builder().tokenKey(jwtToken).user(user).isValid(true).build();
        System.out.println(token);
        try {
            entityManager.persist(token);
            entityManager.flush();
        } catch (PersistenceException ex) {
            throw new RuntimeException("Token for user " + user.getUsername() + " already exists");
        }
        return AuthenticationResponseDTO.builder().token(jwtToken).build();
    }

}
