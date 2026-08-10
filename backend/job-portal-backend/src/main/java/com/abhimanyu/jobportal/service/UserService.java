package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.dto.UserRequestDTO;
import com.abhimanyu.jobportal.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    void deleteUser(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
}