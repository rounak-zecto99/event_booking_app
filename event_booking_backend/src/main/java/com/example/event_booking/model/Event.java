package com.example.event_booking.model;

import java.time.LocalDateTime;

public class Event {

    private Long id;
    private String name;
    private Long venueId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private String status;


    public Event() {
    }

    public Event(
            Long id,
            String name,
            Long venueId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String description,
            String status) {

        this.id = id;
        this.name = name;
        this.venueId = venueId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}