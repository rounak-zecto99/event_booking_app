package com.example.event_booking.repository;

import com.example.event_booking.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User createUser(User user) {

        String sql = """
                INSERT INTO users (name, email)
                VALUES (?, ?)
                RETURNING id
                """;

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                user.getName(),
                user.getEmail()
        );

        return new User(
                id,
                user.getName(),
                user.getEmail()
        );
    }
}