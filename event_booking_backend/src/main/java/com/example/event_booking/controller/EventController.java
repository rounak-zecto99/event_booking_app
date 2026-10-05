package com.example.event_booking.controller;

import com.example.event_booking.dto.EventRequest;
import com.example.event_booking.model.Event;
import com.example.event_booking.model.EventSeat;
import com.example.event_booking.service.EventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event createEvent(@Valid @RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
    @GetMapping("/{eventId}/seats")
    public List<EventSeat> getEventSeats(@PathVariable long eventId) {
        return eventService.getEventSeats(eventId);
    }
}