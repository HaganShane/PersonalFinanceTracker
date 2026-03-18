package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.Budget;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.BudgetRepository;
import com.shanehagan.personalfinance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BudgetServiceTest {

    @Autowired
    BudgetService budgetService;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetBudgetById() {
        User userTest = new User();
        userTest.setFirstName("Test");
        userTest.setLastName("Testing");
        userTest.setPassword("1234");
        userTest.setPhoneNumber("111-222-3333");
        userTest.setDateOfBirth("01-01-01");
        userTest.setEmail("budgettest@email.com");
        userTest.setUserTransactions(null);
        userTest.setUserBudgets(null);
        userTest.setUserCreditCards(null);
        userTest.setUserSavingsGoals(null);
        userRepository.save(userTest);

        Budget expectedBudget = new Budget();
        expectedBudget.setUser(userTest);
        expectedBudget.setCategory("Groceries");
        expectedBudget.setBudgetedAmount(400.00);
        expectedBudget.setMonth("March");
        expectedBudget.setYear(2025);
        expectedBudget.setNotes("Monthly grocery budget");
        budgetRepository.save(expectedBudget);

        Budget actualBudget = budgetService.getBudgetById(expectedBudget.getBudgetId());

        assertEquals(actualBudget, expectedBudget);

        budgetRepository.deleteById(expectedBudget.getBudgetId());
        userRepository.deleteById(userTest.getuId());
    }
}
