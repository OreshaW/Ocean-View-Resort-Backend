package com.example.Ocean_View_Resort.service;

import com.example.Ocean_View_Resort.entity.User;
import com.example.Ocean_View_Resort.enums.Role;
import com.example.Ocean_View_Resort.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Generate Professional User ID
    private String generateUserId() {

        LocalDate today = LocalDate.now();

        long count = userRepository.countByRegistrationDate(today) + 1;

        String formattedNumber = String.format("%03d", count);

        String datePart = today.toString().replace("-", "");

        return "USR-" + datePart + "-" + formattedNumber;
    }

    public User registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setId(generateUserId());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setActive(true);
        user.setRegistrationDate(LocalDate.now());
        user.setTotalBookings(0);

        return userRepository.save(user);
    }

    public User updateUserProfile(String id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use. Please use another email.");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());

        return userRepository.save(existingUser);
    }

    public User createAdmin(User user) {

        user.setId(generateUserId());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_ADMIN);
        user.setActive(true);
        user.setRegistrationDate(LocalDate.now());
        user.setTotalBookings(0);

        return userRepository.save(user);
    }

    public User createManager(User user) {

        user.setId(generateUserId());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_MANAGER);
        user.setActive(true);
        user.setRegistrationDate(LocalDate.now());
        user.setTotalBookings(0);

        return userRepository.save(user);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User toggleUserStatus(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use getActive() because active is a Boolean object
        user.setActive(!Boolean.TRUE.equals(user.getActive()));

        return userRepository.save(user);
    }
}