package com.example.event_booking.controller;

import com.example.event_booking.dto.VenueRequest;
import com.example.event_booking.model.Venue;
import com.example.event_booking.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    public Venue createVenue(@Valid @RequestBody VenueRequest request) {
        return venueService.createVenue(request);
    }
}