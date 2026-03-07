package com.example.Ocean_View_Resort.repository;

import com.example.Ocean_View_Resort.entity.User;
import com.example.Ocean_View_Resort.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    long countByRegistrationDate(LocalDate date);

    List<User> findByRole(Role role);
}