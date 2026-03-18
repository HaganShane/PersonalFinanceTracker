/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * Main Controller to handle requests specific to the login and signup module.
 * Exposes REST endpoints consumed by the React frontend.
 * Returns JSON responses rather than Thymeleaf views.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {
    /**
     * Autowire via field injection
     */
    @Autowired
    private UserService userService;

    /**
     * Processes the user signup request
     * Checks if the email already exists before creating a new account
     * Implements BCrypt Encoding on the password before saving
     * @param user - takes in the user information from the request body
     * @return - returns a ResponseEntity with a message and HTTP status
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> processSignup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        User existingUser = userService.findUserByEmail(user.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            response.put("message", "There is already an account associated with that email. Please try to login instead.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(6, new SecureRandom());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userService.addUser(user);
            response.put("message", "Account created successfully! Please login to get started.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Invalid format was entered, please try again.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Processes the user login request
     * Implements BCrypt Encoding to validate the password
     * @param user - takes in the user information from the request body
     * @return - returns a ResponseEntity with the userId and HTTP status
     * Checks to see if their information is valid before returning access
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> processLogin(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        User existingUser = userService.findUserByEmail(user.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (existingUser == null) {
            response.put("message", "Email not recognized. Be sure it is entered correctly, or create an account.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            response.put("message", "Password is not correct. Be sure it is entered correctly and try again.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            response.put("uId", existingUser.getuId());
            response.put("firstName", existingUser.getFirstName());
            response.put("lastName", existingUser.getLastName());
            response.put("message", "Login successful.");
            return ResponseEntity.ok(response);
        }
    }
}
