package com.example.Ocean_View_Resort.controller;

import com.example.Ocean_View_Resort.dto.CreateManagerRequest;
import com.example.Ocean_View_Resort.entity.User;
import com.example.Ocean_View_Resort.enums.Role;
import com.example.Ocean_View_Resort.service.UserService;
import com.example.Ocean_View_Resort.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public User updateProfile(@PathVariable String id, @RequestBody User user) {
        return userService.updateUserProfile(id, user);
    }

    @PostMapping("/admin")
    public User createAdmin(@RequestBody User user) {
        return userService.createAdmin(user);
    }

    @PostMapping("/manager")
    public User createManager(@Valid @RequestBody CreateManagerRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        return userService.createManager(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/role/{role}")
    public List<User> getByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<User> toggleUserStatus(@PathVariable String id) {
        User updatedUser = userService.toggleUserStatus(id);
        return ResponseEntity.ok(updatedUser);
    }
}