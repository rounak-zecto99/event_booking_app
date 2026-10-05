package com.example.event_booking.service;

import com.example.event_booking.dto.UserRequest;
import com.example.event_booking.model.User;
import com.example.event_booking.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequest request) {

        User user = new User(
                null,
                request.getName(),
                request.getEmail()
        );

        return userRepository.createUser(user);
    }
}