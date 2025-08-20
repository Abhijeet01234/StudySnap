package com.studysnap.controller;

import com.studysnap.model.User;
import com.studysnap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")  // ✅ Don't include ROLE_ here
    public String userHome() {
        return "Welcome to the USER Home!";
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}
