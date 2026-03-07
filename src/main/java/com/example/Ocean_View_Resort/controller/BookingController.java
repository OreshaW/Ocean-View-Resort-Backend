package com.example.Ocean_View_Resort.controller;

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

    @PutMapping("/{id}/status")
    public Booking updateStatus(@PathVariable String id,
            @RequestParam String status) {
        return bookingService.updateStatus(id, status);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }
}