package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.LoginRequestDTO;
import com.abhimanyu.jobportal.dto.LoginResponseDTO;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.exception.UserNotFoundException;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.security.JwtService;
import com.abhimanyu.jobportal.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // =========================
    // CONSTRUCTOR
    // =========================

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // LOGIN
    // =========================

    @Override
    public LoginResponseDTO login(
            LoginRequestDTO dto) {

        // Find user by email
        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Invalid email or password"
                        )
                );

        // Check password
        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // Generate JWT
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Return login response
        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}