package org.cce.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;

public interface AuthenticationService {
    public void register(RegisterRequestDTO request, HttpServletResponse response);
    public void authenticate(AuthenticationRequestDTO request, HttpServletResponse response);
    public void logout(HttpServletResponse response);
}
