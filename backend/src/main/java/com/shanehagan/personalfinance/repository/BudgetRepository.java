/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * BudgetRepository that extends the JpaRepository
 * Used for various functions in controller and service, such as adding, deleting, updating.
 * Added our own method to find budget records specific to a userId - this is used when displaying information on that
 * module page matching a userId entry (so it only displays their information, as opposed to all).
 */

package com.shanehagan.personalfinance.repository;

import com.shanehagan.personalfinance.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    /**
     * Custom method to find all budgets for a specific user
     * @param uId - takes in the user id
     * @return - returns a list of budgets relative to that specific userId
     */
    List<Budget> findBudgetByUser_uId(Integer uId);
}
