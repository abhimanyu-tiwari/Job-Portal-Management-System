package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.dto.UserRequestDTO;
import com.abhimanyu.jobportal.dto.UserResponseDTO;
import com.abhimanyu.jobportal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

    List<UserResponseDTO> getAllUsers();

Optional<UserResponseDTO> getUserById(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User user);
}