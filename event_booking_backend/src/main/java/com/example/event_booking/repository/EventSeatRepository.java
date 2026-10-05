package com.example.event_booking.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import com.example.event_booking.model.EventSeat;
import org.springframework.stereotype.Repository;
import com.example.event_booking.model.EventSeat;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class EventSeatRepository {

    private final JdbcTemplate jdbcTemplate;

    public EventSeatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getSeatPrice(long eventId, long eventSeatId) {

        String sql = """
            SELECT price
            FROM event_seats
            WHERE id = ?
            AND event_id = ?
            """;

        return jdbcTemplate.queryForObject(
                sql,
                BigDecimal.class,
                eventSeatId,
                eventId
        );
    }

    public int bookSeat(long eventId, long eventSeatId) {

        String sql = """
            UPDATE event_seats
            SET status = 'BOOKED'
            WHERE id = ?
            AND event_id = ?
            AND status = 'AVAILABLE'
            """;

        return jdbcTemplate.update(
                sql,
                eventSeatId,
                eventId
        );
    }
    public void createEventSeat(EventSeat eventSeat) {
        String sql = """
        INSERT INTO event_seats
        (event_id, seat_id, status, held_by_user_id, hold_expires_at, price)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                eventSeat.getEventId(),
                eventSeat.getSeatId(),
                eventSeat.getStatus(),
                eventSeat.getHeldByUserId(),
                eventSeat.getHoldExpiresAt(),
                eventSeat.getPrice()
        );
    }
    public List<EventSeat> findByEventId(long eventId) {
        String sql = """
        SELECT id, event_id, seat_id, status,
               held_by_user_id, hold_expires_at, price
        FROM event_seats
        WHERE event_id = ?
        ORDER BY id
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new EventSeat(
                        rs.getLong("id"),
                        rs.getLong("event_id"),
                        rs.getLong("seat_id"),
                        rs.getString("status"),
                        rs.getObject("held_by_user_id", Long.class),
                        rs.getObject("hold_expires_at", java.time.LocalDateTime.class),
                        rs.getBigDecimal("price")
                ),
                eventId
        );
    }
}