/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * BillService class that implements our methods to get user lists of bills, add bill,
 * delete bill, or get a bill by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.Bill;
import com.shanehagan.personalfinance.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    /**
     * Autowire field injection for BillRepository
     */
    @Autowired
    private BillRepository billRepository;

    /**
     * Method to find a list of bills for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of bills for this userId
     */
    public List<Bill> getBillsByUserId(int uId) {
        return billRepository.findBillByUser_uId(uId);
    }

    /**
     * Method to add a bill to the database
     * @param bill - takes in the bill added by the user
     * @return - returns a bill object to be added to the database
     */
    public Bill addBill(Bill bill) {
        return billRepository.save(bill);
    }

    /**
     * Deletes selected bill by its id
     * @param id - id relative to that bill
     */
    public void deleteBillById(int id) {
        billRepository.deleteById(id);
    }

    /**
     * Gets specific bill by its id - used for update
     * @param id - id for the specific bill
     * @return - returns the referenced bill object
     */
    public Bill getBillById(int id) {
        return billRepository.getReferenceById(id);
    }
}