package com.example.event_booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VenueRequest {

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "address must not be blank")
    private String address;

    @NotNull(message = "capacity is required")
    @Positive(message = "capacity must be positive")
    private Integer capacity;

    public VenueRequest() {
    }

    public VenueRequest(String name, String address, Integer capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}