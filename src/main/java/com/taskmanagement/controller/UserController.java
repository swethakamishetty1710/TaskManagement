package com.taskmanagement.controller;

import com.taskmanagement.dto.UserResponse;
import com.taskmanagement.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public UserResponse getProfile(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return UserResponse.fromUser(user);
    }
}