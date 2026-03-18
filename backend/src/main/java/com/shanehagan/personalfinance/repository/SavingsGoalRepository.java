/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * SavingsGoalRepository that extends the JpaRepository
 * Used for various functions in controller and service, such as adding, deleting, updating.
 * Added our own method to find savings goal records specific to a userId - this is used when displaying information on that
 * module page matching a userId entry (so it only displays their information, as opposed to all).
 */

package com.shanehagan.personalfinance.repository;

import com.shanehagan.personalfinance.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Integer> {

    /**
     * Custom method to find all savings goals for a specific user
     * @param uId - takes in the user id
     * @return - returns a list of savings goals relative to that specific userId
     */
    List<SavingsGoal> findSavingsGoalByUser_uId(Integer uId);
}
