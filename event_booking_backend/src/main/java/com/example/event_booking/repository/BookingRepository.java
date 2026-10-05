package com.example.event_booking.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

@Repository
public class BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createBooking(long userId, long eventId) {

        String sql = """
            INSERT INTO bookings (user_id, event_id, status)
            VALUES (?, ?, 'CONFIRMED')
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );

            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void addBookingItem(
            long bookingId,
            long seatId,
            BigDecimal price) {

        String sql = """
        INSERT INTO booking_items (booking_id, event_seat_id, price)
        VALUES (?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                bookingId,
                seatId,
                price
        );
    }
}