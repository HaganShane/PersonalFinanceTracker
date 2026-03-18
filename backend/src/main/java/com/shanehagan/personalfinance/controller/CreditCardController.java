/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * CreditCard Controller to handle requests specific to the credit card module.
 * Able to show the credit card list specific to the user signed in, add a credit card entry for that user,
 * update a credit card entry for that user, or delete a credit card entry for that user.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.CreditCard;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CreditCardController {

    /**
     * Declare some constants here using the final tag
     */
    private final CreditCardService creditCardService;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param creditCardService - declared above, our creditCardService obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public CreditCardController(CreditCardService creditCardService, UserRepository userRepository) {
        this.creditCardService = creditCardService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of credit card entries relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of credit card entries for this user
     */
    @GetMapping("/creditcards/{uId}")
    public ResponseEntity<List<CreditCard>> getCreditCards(@PathVariable("uId") int uId) {
        List<CreditCard> creditCardList = creditCardService.getCreditCardsByUserId(uId);
        return ResponseEntity.ok(creditCardList);
    }

    /**
     * Adds a new credit card entry for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param creditCard - the credit card object received from the React form
     * @return - returns the saved credit card entry as JSON
     */
    @PostMapping("/addCreditCard/{uId}")
    public ResponseEntity<CreditCard> addCreditCard(@PathVariable("uId") int uId, @RequestBody CreditCard creditCard) {
        Optional<User> user = userRepository.findById(uId);
        creditCard.setUser(user.get());

        CreditCard saved = creditCardService.addCreditCard(creditCard);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a credit card entry after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the credit card id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteCreditCard/{uId}/{id}")
    public ResponseEntity<Void> deleteCreditCardById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        creditCardService.deleteCreditCardById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single credit card entry by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the credit card id to be retrieved
     * @return - returns the credit card object as JSON
     */
    @GetMapping("/getCreditCard/{uId}/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable(value = "uId") int uId, @PathVariable(value = "id") int id) {
        CreditCard creditCard = creditCardService.getCreditCardById(id);
        return ResponseEntity.ok(creditCard);
    }

    /**
     * Updates an existing credit card entry for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the credit card id to be updated
     * @param creditCard - the updated credit card object received from the React form
     * @return - returns the updated credit card entry as JSON
     */
    @PutMapping("/updateCreditCard/{uId}/{id}")
    public ResponseEntity<CreditCard> updateCreditCard(@PathVariable(value = "uId") int uId,
                                                        @PathVariable(value = "id") int id,
                                                        @RequestBody CreditCard creditCard) {
        Optional<User> user = userRepository.findById(uId);
        creditCard.setCcId(id);
        creditCard.setUser(user.get());

        CreditCard updated = creditCardService.addCreditCard(creditCard);
        return ResponseEntity.ok(updated);
    }
}
