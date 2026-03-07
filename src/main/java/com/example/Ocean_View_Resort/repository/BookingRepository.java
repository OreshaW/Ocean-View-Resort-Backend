package com.example.Ocean_View_Resort.repository;

import com.example.Ocean_View_Resort.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("""
                SELECT b FROM Booking b
                WHERE b.room.id = :roomId
                AND b.status = 'CONFIRMED'
                AND (
                    (:checkIn BETWEEN b.checkIn AND b.checkOut)
                    OR
                    (:checkOut BETWEEN b.checkIn AND b.checkOut)
                    OR
                    (b.checkIn BETWEEN :checkIn AND :checkOut)
                )
            """)
    List<Booking> findConflictingBookings(
            String roomId,
            LocalDate checkIn,
            LocalDate checkOut);
}