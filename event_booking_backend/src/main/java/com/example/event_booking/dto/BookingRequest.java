package com.example.event_booking.dto;

import jakarta.validation.constraints.Positive;

public class BookingRequest {

    @Positive
    private long eventId;

    @Positive
    private long eventSeatId;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventSeatId() {
        return eventSeatId;
    }

    public void setEventSeatId(long eventSeatId) {
        this.eventSeatId = eventSeatId;
    }
}