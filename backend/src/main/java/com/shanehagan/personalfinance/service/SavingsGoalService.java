/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * SavingsGoalService class that implements our methods to get user lists of savings goals, add savings goal,
 * delete savings goal, or get a savings goal by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.SavingsGoal;
import com.shanehagan.personalfinance.repository.SavingsGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsGoalService {

    /**
     * Autowire field injection for SavingsGoalRepository
     */
    @Autowired
    private SavingsGoalRepository savingsGoalRepository;

    /**
     * Method to find a list of savings goals for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of savings goals for this userId
     */
    public List<SavingsGoal> getSavingsGoalsByUserId(int uId) {
        return savingsGoalRepository.findSavingsGoalByUser_uId(uId);
    }

    /**
     * Method to add a savings goal to the database
     * @param savingsGoal - takes in the savings goal added by the user
     * @return - returns a savings goal object to be added to the database
     */
    public SavingsGoal addSavingsGoal(SavingsGoal savingsGoal) {
        return savingsGoalRepository.save(savingsGoal);
    }

    /**
     * Deletes selected savings goal by its id
     * @param id - id relative to that savings goal
     */
    public void deleteSavingsGoalById(int id) {
        savingsGoalRepository.deleteById(id);
    }

    /**
     * Gets specific savings goal by its id - used for update
     * @param id - id for the specific savings goal
     * @return - returns the referenced savings goal object
     */
    public SavingsGoal getSavingsGoalById(int id) {
        return savingsGoalRepository.getReferenceById(id);
    }
}
