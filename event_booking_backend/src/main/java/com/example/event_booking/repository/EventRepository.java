package com.example.event_booking.repository;

import com.example.event_booking.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Event createEvent(Event event) {

        String sql = """
                INSERT INTO events
                (name, venue_id, start_time, end_time, description, status)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                event.getName(),
                event.getVenueId(),
                event.getStartTime(),
                event.getEndTime(),
                event.getDescription(),
                event.getStatus()
        );

        return new Event(
                id,
                event.getName(),
                event.getVenueId(),
                event.getStartTime(),
                event.getEndTime(),
                event.getDescription(),
                event.getStatus()
        );
    }
    public List<Event> findAll() {
        String sql = """
        SELECT id, name, venue_id, start_time, end_time, description, status
        FROM events
        ORDER BY start_time
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Event(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("venue_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("description"),
                        rs.getString("status")
                )
        );
    }
    public Event findById(long eventId) {
        String sql = """
        SELECT id, name, venue_id, start_time, end_time, description, status
        FROM events
        WHERE id = ?
        """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Event(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("venue_id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("description"),
                        rs.getString("status")
                ),
                eventId
        );
    }
}