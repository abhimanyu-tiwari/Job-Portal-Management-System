package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);
    User updateUser(Long id, User user);

}