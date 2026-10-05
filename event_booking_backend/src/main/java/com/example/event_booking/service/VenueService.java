package com.example.event_booking.service;

import com.example.event_booking.dto.VenueRequest;
import com.example.event_booking.model.Venue;
import com.example.event_booking.model.VenueSeat;
import com.example.event_booking.repository.VenueRepository;
import com.example.event_booking.repository.VenueSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final VenueSeatRepository venueSeatRepository;

    public VenueService(
            VenueRepository venueRepository,
            VenueSeatRepository venueSeatRepository) {

        this.venueRepository = venueRepository;
        this.venueSeatRepository = venueSeatRepository;
    }
    @Transactional
    public Venue createVenue(VenueRequest request) {

        Venue venue = new Venue(
                null,
                request.getName(),
                request.getAddress(),
                request.getCapacity()
        );

        Venue savedVenue = venueRepository.createVenue(venue);

        for (int i = 1; i <= savedVenue.getCapacity(); i++) {

            VenueSeat seat = new VenueSeat(
                    null,
                    savedVenue.getId(),
                    "S" + i
            );

            venueSeatRepository.createSeat(seat);
        }

        return savedVenue;
    }
}