package com.example.Ocean_View_Resort.entity;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private int guests;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDate bookingDate;
}
