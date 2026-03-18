/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * NetWorthService class that implements our methods to get user lists of net worth items, add net worth item,
 * delete net worth item, or get a net worth item by id for updating
 */

package com.shanehagan.personalfinance.service;

import com.shanehagan.personalfinance.model.NetWorth;
import com.shanehagan.personalfinance.repository.NetWorthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetWorthService {

    /**
     * Autowire field injection for NetWorthRepository
     */
    @Autowired
    private NetWorthRepository netWorthRepository;

    /**
     * Method to find a list of net worth items for this specific user
     * @param uId - takes in a userId
     * @return - returns a list of net worth items for this userId
     */
    public List<NetWorth> getNetWorthItemsByUserId(int uId) {
        return netWorthRepository.findNetWorthByUser_uId(uId);
    }

    /**
     * Method to add a net worth item to the database
     * @param netWorth - takes in the net worth item added by the user
     * @return - returns a net worth object to be added to the database
     */
    public NetWorth addNetWorthItem(NetWorth netWorth) {
        return netWorthRepository.save(netWorth);
    }

    /**
     * Deletes selected net worth item by its id
     * @param id - id relative to that net worth item
     */
    public void deleteNetWorthItemById(int id) {
        netWorthRepository.deleteById(id);
    }

    /**
     * Gets specific net worth item by its id - used for update
     * @param id - id for the specific net worth item
     * @return - returns the referenced net worth object
     */
    public NetWorth getNetWorthItemById(int id) {
        return netWorthRepository.getReferenceById(id);
    }
}