/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * User Controller to handle requests specific to the user
 * Returns user information specific to a given userId for the React dashboard
 * Also handles profile updates for the logged in user
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    /**
     * Autowire via field injection
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Returns user information for the given userId
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns the user object as JSON
     */
    @GetMapping("/user/{uId}")
    public ResponseEntity<User> getUserById(@PathVariable("uId") int uId) {
        Optional<User> user = userRepository.findById(uId);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates profile information for the given user
     * Checks if a new password was provided - if so, encodes it before saving
     * If no new password is provided, keeps the existing password unchanged
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param updatedUser - the updated user object received from the React profile form
     * @return - returns the updated user object as JSON
     */
    @PutMapping("/updateProfile/{uId}")
    public ResponseEntity<User> updateProfile(@PathVariable("uId") int uId,
                                              @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(uId);

        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOpt.get();

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

        // Only update the password if a new one was provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }
}