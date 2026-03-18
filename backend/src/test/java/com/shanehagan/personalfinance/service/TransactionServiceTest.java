package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.Transaction;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.TransactionRepository;
import com.shanehagan.personalfinance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetTransactionById() {
        User userTest = new User();
        userTest.setFirstName("Test");
        userTest.setLastName("Testing");
        userTest.setPassword("1234");
        userTest.setPhoneNumber("111-222-3333");
        userTest.setDateOfBirth("01-01-01");
        userTest.setEmail("test@email.com");
        userTest.setUserTransactions(null);
        userTest.setUserBudgets(null);
        userTest.setUserCreditCards(null);
        userTest.setUserSavingsGoals(null);
        userRepository.save(userTest);

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setUser(userTest);
        expectedTransaction.setTransactionName("Test Transaction");
        expectedTransaction.setTransactionType("Expense");
        expectedTransaction.setCategory("Food");
        expectedTransaction.setAmount(50.00);
        expectedTransaction.setDate(LocalDate.now());
        expectedTransaction.setNotes("Test notes");
        transactionRepository.save(expectedTransaction);

        Transaction actualTransaction = transactionService.getTransactionById(expectedTransaction.getTxnId());

        assertEquals(actualTransaction, expectedTransaction);

        transactionRepository.deleteById(expectedTransaction.getTxnId());
        userRepository.deleteById(userTest.getuId());
    }
}
