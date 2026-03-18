/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * Transaction Controller to handle requests specific to the transaction module.
 * Able to show the transaction list specific to the user signed in, add a transaction for that user,
 * update a transaction for that user, or delete a transaction for that user.
 * Also exposes filter endpoints to search transactions by type, category, and date range.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.Transaction;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.TransactionRepository;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    /**
     * Declare some constants here using the final tag
     */
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param transactionService - declared above, our transactionService obj
     * @param transactionRepository - declared above, our transactionRepository obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public TransactionController(TransactionService transactionService,
                                 TransactionRepository transactionRepository,
                                 UserRepository userRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of transactions relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of transactions for this user
     */
    @GetMapping("/transactions/{uId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("uId") int uId) {
        List<Transaction> transactionList = transactionService.getTransactionsByUserId(uId);
        return ResponseEntity.ok(transactionList);
    }

    /**
     * Filters transactions by transaction type for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param type - the transaction type to filter by (Income or Expense)
     * @return - returns a filtered JSON list of transactions for this user
     */
    @GetMapping("/transactions/{uId}/filterByType")
    public ResponseEntity<List<Transaction>> filterByType(@PathVariable("uId") int uId,
                                                          @RequestParam("type") String type) {
        List<Transaction> filtered = transactionRepository.findTransactionByUser_uIdAndTransactionType(uId, type);
        return ResponseEntity.ok(filtered);
    }

    /**
     * Filters transactions by category for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param category - the category string to search by
     * @return - returns a filtered JSON list of transactions for this user
     */
    @GetMapping("/transactions/{uId}/filterByCategory")
    public ResponseEntity<List<Transaction>> filterByCategory(@PathVariable("uId") int uId,
                                                              @RequestParam("category") String category) {
        List<Transaction> filtered = transactionRepository
                .findTransactionByUser_uIdAndCategoryContainingIgnoreCase(uId, category);
        return ResponseEntity.ok(filtered);
    }

    /**
     * Filters transactions by date range for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param startDate - the start of the date range
     * @param endDate - the end of the date range
     * @return - returns a filtered JSON list of transactions for this user
     */
    @GetMapping("/transactions/{uId}/filterByDate")
    public ResponseEntity<List<Transaction>> filterByDate(
            @PathVariable("uId") int uId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Transaction> filtered = transactionRepository
                .findTransactionByUser_uIdAndDateBetween(uId, startDate, endDate);
        return ResponseEntity.ok(filtered);
    }

    /**
     * Adds a new transaction for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param transaction - the transaction object received from the React form
     * @return - returns the saved transaction as JSON
     */
    @PostMapping("/addTransaction/{uId}")
    public ResponseEntity<Transaction> addTransaction(@PathVariable("uId") int uId,
                                                      @RequestBody Transaction transaction) {
        Optional<User> user = userRepository.findById(uId);
        transaction.setUser(user.get());

        Transaction saved = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a transaction after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the transaction id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteTransaction/{uId}/{id}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable(value = "uId") int uId,
                                                      @PathVariable(value = "id") int id) {
        transactionService.deleteTransactionById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single transaction by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the transaction id to be retrieved
     * @return - returns the transaction object as JSON
     */
    @GetMapping("/getTransaction/{uId}/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable(value = "uId") int uId,
                                                          @PathVariable(value = "id") int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Updates an existing transaction for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the transaction id to be updated
     * @param transaction - the updated transaction object received from the React form
     * @return - returns the updated transaction as JSON
     */
    @PutMapping("/updateTransaction/{uId}/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable(value = "uId") int uId,
                                                         @PathVariable(value = "id") int id,
                                                         @RequestBody Transaction transaction) {
        Optional<User> user = userRepository.findById(uId);
        transaction.setTxnId(id);
        transaction.setUser(user.get());

        Transaction updated = transactionService.addTransaction(transaction);
        return ResponseEntity.ok(updated);
    }
}