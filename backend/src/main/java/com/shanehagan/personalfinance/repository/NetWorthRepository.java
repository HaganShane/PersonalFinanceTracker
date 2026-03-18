/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * NetWorthRepository that extends the JpaRepository
 * Used for various functions in controller and service, such as adding, deleting, updating.
 * Added our own method to find net worth records specific to a userId - this is used when displaying information on that
 * module page matching a userId entry (so it only displays their information, as opposed to all).
 */

package com.shanehagan.personalfinance.repository;

import com.shanehagan.personalfinance.model.NetWorth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NetWorthRepository extends JpaRepository<NetWorth, Integer> {

    /**
     * Custom method to find all net worth items for a specific user
     * @param uId - takes in the user id
     * @return - returns a list of net worth items relative to that specific userId
     */
    List<NetWorth> findNetWorthByUser_uId(Integer uId);
}