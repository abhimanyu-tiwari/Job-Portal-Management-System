package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.UserRequestDTO;
import com.abhimanyu.jobportal.dto.UserResponseDTO;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.enums.Role;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE USER
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

    // GET ALL USERS
    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // GET USER BY ID
    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {

        return userRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    // DELETE USER
    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }

    // UPDATE USER
    @Override
    public User updateUser(Long id, User user) {

        Optional<User> existingUserOptional =
                userRepository.findById(id);

        if (existingUserOptional.isPresent()) {

            User existingUser = existingUserOptional.get();

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhone(user.getPhone());
            existingUser.setRole(user.getRole());

            return userRepository.save(existingUser);

        } else {

            throw new RuntimeException(
                    "User not found with id: " + id
            );
        }
    }

    // ENTITY -> RESPONSE DTO
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