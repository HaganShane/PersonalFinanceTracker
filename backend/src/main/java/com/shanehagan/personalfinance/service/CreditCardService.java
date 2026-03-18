/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * CreditCardService class that implements our methods to get user lists of credit card entries, add credit card entry,
 * delete credit card entry, or get a credit card entry by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.CreditCard;
import com.shanehagan.personalfinance.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {

    /**
     * Autowire field injection for CreditCardRepository
     */
    @Autowired
    private CreditCardRepository creditCardRepository;

    /**
     * Method to find a list of credit card entries for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of credit card entries for this userId
     */
    public List<CreditCard> getCreditCardsByUserId(int uId) {
        return creditCardRepository.findCreditCardByUser_uId(uId);
    }

    /**
     * Method to add a credit card entry to the database
     * @param creditCard - takes in the credit card entry added by the user
     * @return - returns a credit card object to be added to the database
     */
    public CreditCard addCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    /**
     * Deletes selected credit card entry by its id
     * @param id - id relative to that credit card entry
     */
    public void deleteCreditCardById(int id) {
        creditCardRepository.deleteById(id);
    }

    /**
     * Gets specific credit card entry by its id - used for update
     * @param id - id for the specific credit card entry
     * @return - returns the referenced credit card object
     */
    public CreditCard getCreditCardById(int id) {
        return creditCardRepository.getReferenceById(id);
    }
}
