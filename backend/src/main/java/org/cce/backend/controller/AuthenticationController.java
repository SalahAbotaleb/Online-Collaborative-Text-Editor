package org.cce.backend.controller;

import lombok.RequiredArgsConstructor;
import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.dto.AuthenticationResponseDTO;
import org.cce.backend.dto.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody AuthenticationRequestDTO request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
//    @PostMapping("/user")
//    public String createUser(@RequestBody UserRegisterationDTO userRegisterationDTO){
//        return userRegisterationDTO.getUsername();
//    }
}
