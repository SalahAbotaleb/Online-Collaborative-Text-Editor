package org.cce.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;

public interface AuthenticationService {
    public AuthenticationResponseDTO register(RegisterRequestDTO requestAuthenticationResponseDTO);
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    public void logout();
}
