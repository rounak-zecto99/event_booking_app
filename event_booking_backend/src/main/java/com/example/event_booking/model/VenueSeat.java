package com.example.event_booking.model;

public class VenueSeat {

    private Long id;
    private Long venueId;
    private String seatNumber;

    public VenueSeat() {
    }

    public VenueSeat(Long id, Long venueId, String seatNumber) {
        this.id = id;
        this.venueId = venueId;
        this.seatNumber = seatNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}