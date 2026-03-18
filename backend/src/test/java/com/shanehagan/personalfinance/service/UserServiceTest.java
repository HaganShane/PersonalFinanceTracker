package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testFindUserByEmail() {
        User userTest = new User();
        userTest.setFirstName("Test");
        userTest.setLastName("Testing");
        userTest.setPassword("1234");
        userTest.setPhoneNumber("111-222-3333");
        userTest.setDateOfBirth("01-01-01");
        userTest.setEmail("userservicetest@email.com");
        userTest.setUserTransactions(null);
        userTest.setUserBudgets(null);
        userTest.setUserCreditCards(null);
        userTest.setUserSavingsGoals(null);
        userRepository.save(userTest);

        User foundUser = userService.findUserByEmail("userservicetest@email.com");

        assertEquals(foundUser.getEmail(), userTest.getEmail());

        userRepository.deleteById(userTest.getuId());
    }
}
