package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.dto.LoginRequestDTO;
import com.abhimanyu.jobportal.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}