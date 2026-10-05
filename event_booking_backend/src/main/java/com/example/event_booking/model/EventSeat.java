package com.example.event_booking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSeat {

    private Long id;
    private Long eventId;
    private Long seatId;
    private String status;
    private Long heldByUserId;
    private LocalDateTime holdExpiresAt;
    private BigDecimal price;

    public EventSeat() {
    }

    public EventSeat(
            Long id,
            Long eventId,
            Long seatId,
            String status,
            Long heldByUserId,
            LocalDateTime holdExpiresAt,
            BigDecimal price) {

        this.id = id;
        this.eventId = eventId;
        this.seatId = seatId;
        this.status = status;
        this.heldByUserId = heldByUserId;
        this.holdExpiresAt = holdExpiresAt;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHeldByUserId() {
        return heldByUserId;
    }

    public void setHeldByUserId(Long heldByUserId) {
        this.heldByUserId = heldByUserId;
    }

    public LocalDateTime getHoldExpiresAt() {
        return holdExpiresAt;
    }

    public void setHoldExpiresAt(LocalDateTime holdExpiresAt) {
        this.holdExpiresAt = holdExpiresAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}