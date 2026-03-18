/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * TransactionService class that implements our methods to get user lists of transactions, add transaction,
 * delete transaction, or get a transaction by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.Transaction;
import com.shanehagan.personalfinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    /**
     * Autowire field injection for TransactionRepository
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Method to find a list of transactions for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of transactions for this userId
     */
    public List<Transaction> getTransactionsByUserId(int uId) {
        return transactionRepository.findTransactionByUser_uId(uId);
    }

    /**
     * Method to add a transaction to the database
     * @param transaction - takes in the transaction added by the user
     * @return - returns a transaction object to be added to the database
     */
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Deletes selected transaction by its id
     * @param id - id relative to that transaction
     */
    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Gets specific transaction by its id - used for update
     * @param id - id for the specific transaction
     * @return - returns the referenced transaction object
     */
    public Transaction getTransactionById(int id) {
        return transactionRepository.getReferenceById(id);
    }
}
