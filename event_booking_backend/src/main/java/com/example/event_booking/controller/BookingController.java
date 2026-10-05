package com.example.event_booking.controller;

import com.example.event_booking.dto.BookingRequest;
import com.example.event_booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<String> bookSeat(
            @Valid @RequestBody BookingRequest request) {

        long userId = 1; // temporary until authentication

        boolean success = bookingService.bookSeat(
                userId,
                request.getEventId(),
                request.getEventSeatId()
        );

        if (success) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Booking successful");
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Seat is not available");
    }
}