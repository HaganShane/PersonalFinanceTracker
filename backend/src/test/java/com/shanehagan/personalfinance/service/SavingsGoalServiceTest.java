package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.SavingsGoal;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.SavingsGoalRepository;
import com.shanehagan.personalfinance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SavingsGoalServiceTest {

    @Autowired
    SavingsGoalService savingsGoalService;

    @Autowired
    SavingsGoalRepository savingsGoalRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetSavingsGoalById() {
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

        SavingsGoal expectedGoal = new SavingsGoal();
        expectedGoal.setUser(userTest);
        expectedGoal.setGoalName("Emergency Fund");
        expectedGoal.setTargetAmount(5000.00);
        expectedGoal.setCurrentAmount(1000.00);
        expectedGoal.setTargetDate(LocalDate.of(2025, 12, 31));
        expectedGoal.setNotes("Test notes");
        savingsGoalRepository.save(expectedGoal);

        SavingsGoal actualGoal = savingsGoalService.getSavingsGoalById(expectedGoal.getGoalId());

        assertEquals(actualGoal, expectedGoal);

        savingsGoalRepository.deleteById(expectedGoal.getGoalId());
        userRepository.deleteById(userTest.getuId());
    }
}
