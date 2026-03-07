package com.example.Ocean_View_Resort.service;

import com.example.Ocean_View_Resort.entity.Booking;
import com.example.Ocean_View_Resort.entity.Room;
import com.example.Ocean_View_Resort.repository.BookingRepository;
import com.example.Ocean_View_Resort.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository,
            RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public Booking createBooking(Booking booking) {

        // 1️⃣ Validate dates
        if (booking.getCheckIn().isAfter(booking.getCheckOut()) ||
                booking.getCheckIn().isEqual(booking.getCheckOut())) {
            throw new RuntimeException("Invalid booking dates");
        }

        // 2️⃣ Get Room
        Room room = roomRepository.findById(booking.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3️⃣ Check max guests
        if (booking.getGuests() > room.getMaxGuests()) {
            throw new RuntimeException("Guest count exceeds room capacity");
        }

        // 4️⃣ Check date conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(),
                booking.getCheckIn(),
                booking.getCheckOut());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        // 5️⃣ Calculate total price automatically
        long days = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut());

        double totalPrice = days * room.getPrice();

        // 6️⃣ Set system fields
        booking.setId(UUID.randomUUID().toString());
        booking.setRoom(room);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");
        booking.setBookingDate(LocalDate.now());

        return bookingRepository.save(booking);
    }

    public Booking updateStatus(String bookingId, String status) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(status.toUpperCase());

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}