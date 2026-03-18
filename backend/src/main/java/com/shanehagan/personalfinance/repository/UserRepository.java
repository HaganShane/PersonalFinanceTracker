/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * UserRepository that extends the JpaRepository
 * Custom method to find a user based on their email - useful when signing up and logging in to check if it exists
 * or is valid.
 */

package com.shanehagan.personalfinance.repository;

import com.shanehagan.personalfinance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Used to find a specific user based on their email
     * @param email - takes in an email
     * @return - returns a user object
     */
    User findByEmail(String email);
}
