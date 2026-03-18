/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * TransactionRepository that extends the JpaRepository
 * Used for various functions in controller and service, such as adding, deleting, updating.
 * Added our own method to find transaction records specific to a userId - this is used when displaying information on that
 * module page matching a userId entry (so it only displays their information, as opposed to all).
 * Added search and filter methods to allow users to filter transactions by type, category, and date range.
 */

package com.shanehagan.personalfinance.repository;

import com.shanehagan.personalfinance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Custom method to find all transactions for a specific user
     * @param uId - takes in the user id
     * @return - returns a list of transactions relative to that specific userId
     */
    List<Transaction> findTransactionByUser_uId(Integer uId);

    /**
     * Custom method to find all transactions for a specific user filtered by transaction type
     * @param uId - takes in the user id
     * @param transactionType - takes in the transaction type (Income or Expense)
     * @return - returns a filtered list of transactions relative to that specific userId and type
     */
    List<Transaction> findTransactionByUser_uIdAndTransactionType(Integer uId, String transactionType);

    /**
     * Custom method to find all transactions for a specific user filtered by category
     * @param uId - takes in the user id
     * @param category - takes in the category string to filter by
     * @return - returns a filtered list of transactions relative to that specific userId and category
     */
    List<Transaction> findTransactionByUser_uIdAndCategoryContainingIgnoreCase(Integer uId, String category);

    /**
     * Custom method to find all transactions for a specific user within a date range
     * @param uId - takes in the user id
     * @param startDate - the start of the date range
     * @param endDate - the end of the date range
     * @return - returns a filtered list of transactions relative to that specific userId and date range
     */
    List<Transaction> findTransactionByUser_uIdAndDateBetween(Integer uId, LocalDate startDate, LocalDate endDate);
}