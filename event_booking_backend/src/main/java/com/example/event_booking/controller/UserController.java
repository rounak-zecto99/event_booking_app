package com.example.event_booking.controller;

import com.example.event_booking.dto.UserRequest;
import com.example.event_booking.model.User;
import com.example.event_booking.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }
}