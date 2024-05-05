package org.cce.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;

public interface AuthenticationService {
    public AuthenticationResponseDTO register(RegisterRequestDTO request);
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    public void logout(HttpServletRequest request);
}
