/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * BudgetService class that implements our methods to get user lists of budgets, add budget,
 * delete budget, or get a budget by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.Budget;
import com.shanehagan.personalfinance.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    /**
     * Autowire field injection for BudgetRepository
     */
    @Autowired
    private BudgetRepository budgetRepository;

    /**
     * Method to find a list of budgets for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of budgets for this userId
     */
    public List<Budget> getBudgetsByUserId(int uId) {
        return budgetRepository.findBudgetByUser_uId(uId);
    }

    /**
     * Method to add a budget to the database
     * @param budget - takes in the budget added by the user
     * @return - returns a budget object to be added to the database
     */
    public Budget addBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    /**
     * Deletes selected budget by its id
     * @param id - id relative to that budget
     */
    public void deleteBudgetById(int id) {
        budgetRepository.deleteById(id);
    }

    /**
     * Gets specific budget by its id - used for update
     * @param id - id for the specific budget
     * @return - returns the referenced budget object
     */
    public Budget getBudgetById(int id) {
        return budgetRepository.getReferenceById(id);
    }
}
