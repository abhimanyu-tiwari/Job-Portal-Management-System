package com.abhimanyu.jobportal.controller;

import com.abhimanyu.jobportal.dto.LoginRequestDTO;
import com.abhimanyu.jobportal.dto.LoginResponseDTO;
import com.abhimanyu.jobportal.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        return authService.login(loginRequestDTO);
    }
}