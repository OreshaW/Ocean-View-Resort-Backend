package com.example.Ocean_View_Resort.service;

import com.example.Ocean_View_Resort.entity.Booking;
import com.example.Ocean_View_Resort.entity.BookingStatus;
import com.example.Ocean_View_Resort.entity.Room;
import com.example.Ocean_View_Resort.entity.User;
import com.example.Ocean_View_Resort.repository.BookingRepository;
import com.example.Ocean_View_Resort.repository.RoomRepository;
import com.example.Ocean_View_Resort.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
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

        // 3️⃣ Get User
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4️⃣ Check max guests
        if (booking.getGuests() > room.getMaxGuests()) {
            throw new RuntimeException("Guest count exceeds room capacity");
        }

        // 5️⃣ Check date conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(),
                booking.getCheckIn(),
                booking.getCheckOut());

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room already booked for selected dates");
        }

        // 6️⃣ Calculate total price automatically
        long days = ChronoUnit.DAYS.between(
                booking.getCheckIn(),
                booking.getCheckOut());

        double totalPrice = days * room.getPrice();

        // 7️⃣ Set system fields
        booking.setId(generateReservationId());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingDate(LocalDate.now());

        return bookingRepository.save(booking);
    }

    public Booking updateStatus(String bookingId, String status) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(BookingStatus.fromString(status));

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    // ✅ Current bookings
    public List<Booking> getCurrentBookings(String userId) {

        LocalDate today = LocalDate.now();

        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId))
                .filter(b -> b.getCheckOut().isAfter(today.minusDays(1)))
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

    // ✅ Past bookings
    public List<Booking> getPastBookings(String userId) {

        LocalDate today = LocalDate.now();

        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId))
                .filter(b -> b.getCheckOut().isBefore(today)
                        || b.getStatus() == BookingStatus.COMPLETED
                        || b.getStatus() == BookingStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    private String generateReservationId() {

        String datePart = LocalDate.now().toString().replace("-", "");

        long count = bookingRepository.count() + 1;

        return "RES-" + datePart + "-" + String.format("%04d", count);
    }
}