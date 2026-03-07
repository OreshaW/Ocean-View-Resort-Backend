package com.example.Ocean_View_Resort.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    private String id;

    @ManyToOne
    @JsonIgnoreProperties("bookings")
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String userId;
    private String userName;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private Integer guests;
    private Double totalPrice;

    private String status; // CONFIRMED, COMPLETED, CANCELLED

    private LocalDate bookingDate;
}
