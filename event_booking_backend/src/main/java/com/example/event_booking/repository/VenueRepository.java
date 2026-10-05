package com.example.event_booking.repository;

import com.example.event_booking.model.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VenueRepository {

    private final JdbcTemplate jdbcTemplate;

    public VenueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Venue createVenue(Venue venue) {

        String sql = """
                INSERT INTO venues (name, address, capacity)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                venue.getName(),
                venue.getAddress(),
                venue.getCapacity()
        );

        return new Venue(
                id,
                venue.getName(),
                venue.getAddress(),
                venue.getCapacity()
        );
    }
}