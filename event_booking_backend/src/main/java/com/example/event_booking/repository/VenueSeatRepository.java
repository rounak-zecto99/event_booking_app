package com.example.event_booking.repository;

import com.example.event_booking.model.VenueSeat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VenueSeatRepository {

    private final JdbcTemplate jdbcTemplate;

    public VenueSeatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSeat(VenueSeat seat) {

        String sql = """
                INSERT INTO venue_seats (venue_id, seat_number)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(
                sql,
                seat.getVenueId(),
                seat.getSeatNumber()
        );
    }
    public List<VenueSeat> findByVenueId(Long venueId) {

        String sql = """
            SELECT id, venue_id, seat_number
            FROM venue_seats
            WHERE venue_id = ?
            ORDER BY id
            """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new VenueSeat(
                        rs.getLong("id"),
                        rs.getLong("venue_id"),
                        rs.getString("seat_number")
                ),
                venueId
        );
    }
}