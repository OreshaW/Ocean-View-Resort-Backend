package com.example.Ocean_View_Resort.controller;

import com.example.Ocean_View_Resort.dto.BookingStatusUpdateRequest;
import com.example.Ocean_View_Resort.entity.Booking;
import com.example.Ocean_View_Resort.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PutMapping(value = "/{id}/status", params = "status")
    public Booking updateStatusParam(@PathVariable String id, @RequestParam String status) {
        return bookingService.updateStatus(id, status);
    }

    @PutMapping(value = "/{id}/status", consumes = "application/json")
    public Booking updateStatusBody(@PathVariable String id, @RequestBody BookingStatusUpdateRequest body) {
        return bookingService.updateStatus(id, body != null ? body.getStatus() : null);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // @GetMapping("/user/{userId}")
    // public List<Booking> getBookingsByUser(
    // @PathVariable Long userId,
    // @RequestParam(required = false) String status)

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(
            @PathVariable String userId,
            @RequestParam(required = false) String status) {

        if (status == null) {
            return bookingService.getBookingsByUser(userId);
        } else if (status.equalsIgnoreCase("current")) {
            return bookingService.getCurrentBookings(userId);
        } else if (status.equalsIgnoreCase("past")) {
            return bookingService.getPastBookings(userId);
        } else {
            return bookingService.getBookingsByUser(userId);
        }
    }
}
