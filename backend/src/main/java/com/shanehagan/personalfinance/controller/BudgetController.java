/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * Budget Controller to handle requests specific to the budget module.
 * Able to show the budget list specific to the user signed in, add a budget for that user,
 * update a budget for that user, or delete a budget for that user.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.Budget;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BudgetController {

    /**
     * Declare some constants here using the final tag
     */
    private final BudgetService budgetService;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param budgetService - declared above, our budgetService obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public BudgetController(BudgetService budgetService, UserRepository userRepository) {
        this.budgetService = budgetService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of budgets relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of budgets for this user
     */
    @GetMapping("/budgets/{uId}")
    public ResponseEntity<List<Budget>> getBudgets(@PathVariable("uId") int uId) {
        List<Budget> budgetList = budgetService.getBudgetsByUserId(uId);
        return ResponseEntity.ok(budgetList);
    }

    /**
     * Adds a new budget for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param budget - the budget object received from the React form
     * @return - returns the saved budget as JSON
     */
    @PostMapping("/addBudget/{uId}")
    public ResponseEntity<Budget> addBudget(@PathVariable("uId") int uId, @RequestBody Budget budget) {
        Optional<User> user = userRepository.findById(uId);
        budget.setUser(user.get());

        Budget saved = budgetService.addBudget(budget);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a budget after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the budget id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteBudget/{uId}/{id}")
    public ResponseEntity<Void> deleteBudgetById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        budgetService.deleteBudgetById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single budget by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the budget id to be retrieved
     * @return - returns the budget object as JSON
     */
    @GetMapping("/getBudget/{uId}/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        Budget budget = budgetService.getBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    /**
     * Updates an existing budget for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the budget id to be updated
     * @param budget - the updated budget object received from the React form
     * @return - returns the updated budget as JSON
     */
    @PutMapping("/updateBudget/{uId}/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable(value = "uId") int uId,
                                                @PathVariable(value = "id") int id,
                                                @RequestBody Budget budget) {
        Optional<User> user = userRepository.findById(uId);
        budget.setBudgetId(id);
        budget.setUser(user.get());

        Budget updated = budgetService.addBudget(budget);
        return ResponseEntity.ok(updated);
    }
}
