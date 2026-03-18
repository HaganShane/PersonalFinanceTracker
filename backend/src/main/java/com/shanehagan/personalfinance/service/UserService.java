/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * UserService class that implements our methods to find a user by their email and add a new user
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * Autowire via field injection
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Find a user by a given email
     * @param email - string of an email passed in
     * @return - returns a user object based on that string
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Adds a user to the database
     * @param user - takes in a user object, sets those values to the new user and saves it to the db
     */
    public void addUser(User user) {
        User newUser = new User();

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setPassword(user.getPassword());

        userRepository.save(newUser);
    }
}
