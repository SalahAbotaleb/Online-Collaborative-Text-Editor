package org.cce.backend.service;

import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthenticationService {
    public AuthenticationResponseDTO register(RegisterRequestDTO request);
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
}
