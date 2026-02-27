package com.example.Ocean_View_Resort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ocean_View_Resort.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
