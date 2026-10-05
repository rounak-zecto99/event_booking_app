package com.example.event_booking.service;

import com.example.event_booking.dto.EventRequest;
import com.example.event_booking.model.Event;
import com.example.event_booking.model.EventSeat;
import com.example.event_booking.model.VenueSeat;
import com.example.event_booking.repository.EventRepository;
import com.example.event_booking.repository.EventSeatRepository;
import com.example.event_booking.repository.VenueSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueSeatRepository venueSeatRepository;
    private final EventSeatRepository eventSeatRepository;

    public EventService(
            EventRepository eventRepository,
            VenueSeatRepository venueSeatRepository,
            EventSeatRepository eventSeatRepository) {

        this.eventRepository = eventRepository;
        this.venueSeatRepository = venueSeatRepository;
        this.eventSeatRepository = eventSeatRepository;
    }

    @Transactional
    public Event createEvent(EventRequest request) {

        Event event = new Event(
                null,
                request.getName(),
                request.getVenueId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDescription(),
                "ACTIVE"
        );

        Event savedEvent = eventRepository.createEvent(event);

        List<VenueSeat> venueSeats =
                venueSeatRepository.findByVenueId(savedEvent.getVenueId());

        for (VenueSeat venueSeat : venueSeats) {

            EventSeat eventSeat = new EventSeat(
                    null,
                    savedEvent.getId(),
                    venueSeat.getId(),
                    "AVAILABLE",
                    null,
                    null,
                    request.getPrice()
            );

            eventSeatRepository.createEventSeat(eventSeat);
        }

        return savedEvent;
    }
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    public Event getEvent(long eventId) {
        return eventRepository.findById(eventId);
    }
    public List<EventSeat> getEventSeats(long eventId) {
        return eventSeatRepository.findByEventId(eventId);
    }
}