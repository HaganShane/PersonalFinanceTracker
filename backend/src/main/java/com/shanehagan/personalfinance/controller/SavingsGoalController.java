/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * SavingsGoal Controller to handle requests specific to the savings goal module.
 * Able to show the savings goal list specific to the user signed in, add a savings goal for that user,
 * update a savings goal for that user, or delete a savings goal for that user.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.SavingsGoal;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.SavingsGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SavingsGoalController {

    /**
     * Declare some constants here using the final tag
     */
    private final SavingsGoalService savingsGoalService;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param savingsGoalService - declared above, our savingsGoalService obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public SavingsGoalController(SavingsGoalService savingsGoalService, UserRepository userRepository) {
        this.savingsGoalService = savingsGoalService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of savings goals relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of savings goals for this user
     */
    @GetMapping("/savingsgoals/{uId}")
    public ResponseEntity<List<SavingsGoal>> getSavingsGoals(@PathVariable("uId") int uId) {
        List<SavingsGoal> savingsGoalList = savingsGoalService.getSavingsGoalsByUserId(uId);
        return ResponseEntity.ok(savingsGoalList);
    }

    /**
     * Adds a new savings goal for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param savingsGoal - the savings goal object received from the React form
     * @return - returns the saved savings goal as JSON
     */
    @PostMapping("/addSavingsGoal/{uId}")
    public ResponseEntity<SavingsGoal> addSavingsGoal(@PathVariable("uId") int uId, @RequestBody SavingsGoal savingsGoal) {
        Optional<User> user = userRepository.findById(uId);
        savingsGoal.setUser(user.get());

        SavingsGoal saved = savingsGoalService.addSavingsGoal(savingsGoal);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a savings goal after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the savings goal id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteSavingsGoal/{uId}/{id}")
    public ResponseEntity<Void> deleteSavingsGoalById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        savingsGoalService.deleteSavingsGoalById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single savings goal by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the savings goal id to be retrieved
     * @return - returns the savings goal object as JSON
     */
    @GetMapping("/getSavingsGoal/{uId}/{id}")
    public ResponseEntity<SavingsGoal> getSavingsGoalById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        SavingsGoal savingsGoal = savingsGoalService.getSavingsGoalById(id);
        return ResponseEntity.ok(savingsGoal);
    }

    /**
     * Updates an existing savings goal for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the savings goal id to be updated
     * @param savingsGoal - the updated savings goal object received from the React form
     * @return - returns the updated savings goal as JSON
     */
    @PutMapping("/updateSavingsGoal/{uId}/{id}")
    public ResponseEntity<SavingsGoal> updateSavingsGoal(@PathVariable(value = "uId") int uId,
                                                          @PathVariable(value = "id") int id,
                                                          @RequestBody SavingsGoal savingsGoal) {
        Optional<User> user = userRepository.findById(uId);
        savingsGoal.setGoalId(id);
        savingsGoal.setUser(user.get());

        SavingsGoal updated = savingsGoalService.addSavingsGoal(savingsGoal);
        return ResponseEntity.ok(updated);
    }
}
