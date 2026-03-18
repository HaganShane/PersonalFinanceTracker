/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * Bill Controller to handle requests specific to the bill module.
 * Able to show the bill list specific to the user signed in, add a bill for that user,
 * update a bill for that user, or delete a bill for that user.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.Bill;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BillController {

    /**
     * Declare some constants here using the final tag
     */
    private final BillService billService;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param billService - declared above, our billService obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public BillController(BillService billService, UserRepository userRepository) {
        this.billService = billService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of bills relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of bills for this user
     */
    @GetMapping("/bills/{uId}")
    public ResponseEntity<List<Bill>> getBills(@PathVariable("uId") int uId) {
        List<Bill> billList = billService.getBillsByUserId(uId);
        return ResponseEntity.ok(billList);
    }

    /**
     * Adds a new bill for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param bill - the bill object received from the React form
     * @return - returns the saved bill as JSON
     */
    @PostMapping("/addBill/{uId}")
    public ResponseEntity<Bill> addBill(@PathVariable("uId") int uId, @RequestBody Bill bill) {
        Optional<User> user = userRepository.findById(uId);
        bill.setUser(user.get());

        Bill saved = billService.addBill(bill);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a bill after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the bill id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteBill/{uId}/{id}")
    public ResponseEntity<Void> deleteBillById(@PathVariable(value = "uId") int uId,
                                               @PathVariable(value = "id") int id) {
        billService.deleteBillById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single bill by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the bill id to be retrieved
     * @return - returns the bill object as JSON
     */
    @GetMapping("/getBill/{uId}/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable(value = "uId") int uId,
                                            @PathVariable(value = "id") int id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    /**
     * Updates an existing bill for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the bill id to be updated
     * @param bill - the updated bill object received from the React form
     * @return - returns the updated bill as JSON
     */
    @PutMapping("/updateBill/{uId}/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable(value = "uId") int uId,
                                           @PathVariable(value = "id") int id,
                                           @RequestBody Bill bill) {
        Optional<User> user = userRepository.findById(uId);
        bill.setBillId(id);
        bill.setUser(user.get());

        Bill updated = billService.addBill(bill);
        return ResponseEntity.ok(updated);
    }
}