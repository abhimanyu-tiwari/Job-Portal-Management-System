package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.UserRequestDTO;
import com.abhimanyu.jobportal.dto.UserResponseDTO;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.enums.Role;
import com.abhimanyu.jobportal.exception.UserNotFoundException;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO saveUser(UserRequestDTO dto) {

        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setRole(Role.valueOf(dto.getRole()));

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return convertToResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {

            throw new UserNotFoundException(
                    "User not found with id: " + id
            );
        }

        userRepository.deleteById(id);
    }

   @Override
public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

    User existingUser = userRepository.findById(id)
            .orElseThrow(() ->
                    new UserNotFoundException(
                            "User not found with id: " + id
                    )
            );

    existingUser.setFirstName(dto.getFirstName());
    existingUser.setLastName(dto.getLastName());
    existingUser.setEmail(dto.getEmail());
    existingUser.setPassword(dto.getPassword());
    existingUser.setPhone(dto.getPhone());
    existingUser.setRole(Role.valueOf(dto.getRole()));

    User updatedUser = userRepository.save(existingUser);

    return convertToResponseDTO(updatedUser);
}


    private UserResponseDTO convertToResponseDTO(User user) {

        UserResponseDTO response = new UserResponseDTO();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        if (user.getRole() != null) {
            response.setRole(user.getRole().name());
        }

        return response;
    }
}