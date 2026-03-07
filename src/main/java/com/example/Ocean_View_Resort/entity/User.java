package com.example.Ocean_View_Resort.entity;

import com.example.Ocean_View_Resort.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDate registrationDate;

    private Boolean active;

    private Integer totalBookings;

    @PrePersist
    public void onCreate() {
        this.registrationDate = LocalDate.now();
        this.active = true;
        this.totalBookings = 0;
    }
}