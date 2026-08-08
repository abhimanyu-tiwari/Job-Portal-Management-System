package com.abhimanyu.jobportal.controller;

import com.abhimanyu.jobportal.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abhimanyu.jobportal.entity.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
   @GetMapping
public List<User> getAllUsers() {
    return userService.getAllUsers();
}
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.saveUser(user);
}
@GetMapping("/{id}")
public Optional<User> getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
}
@DeleteMapping("/{id}")
public void deleteUser(@PathVariable Long id) {
 userService.deleteUser(id);
}
@PutMapping("/{id}")
public User updateUser(@PathVariable Long id, @RequestBody User user) {
    return userService.updateUser(id, user);
}
}