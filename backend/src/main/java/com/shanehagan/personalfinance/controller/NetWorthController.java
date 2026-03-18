/**
 * @Author: Shane Hagan
 * Date: 3/16/2025
 * NetWorth Controller to handle requests specific to the net worth module.
 * Able to show the net worth list specific to the user signed in, add a net worth item for that user,
 * update a net worth item for that user, or delete a net worth item for that user.
 * Uses @GetMapping to retrieve, @PostMapping to add, @PutMapping to update, @DeleteMapping to delete,
 * @PathVariables to pass the userId around, and @RequestBody to receive JSON from React.
 */

package com.shanehagan.personalfinance.controller;

import com.shanehagan.personalfinance.model.NetWorth;
import com.shanehagan.personalfinance.model.User;
import com.shanehagan.personalfinance.repository.UserRepository;
import com.shanehagan.personalfinance.service.NetWorthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class NetWorthController {

    /**
     * Declare some constants here using the final tag
     */
    private final NetWorthService netWorthService;
    private final UserRepository userRepository;

    /**
     * Autowire via constructor injection rather than field injection
     * @param netWorthService - declared above, our netWorthService obj
     * @param userRepository - declared above, our userRepository obj
     */
    @Autowired
    public NetWorthController(NetWorthService netWorthService, UserRepository userRepository) {
        this.netWorthService = netWorthService;
        this.userRepository = userRepository;
    }

    /**
     * Returns the list of net worth items relative to the user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @return - returns a JSON list of net worth items for this user
     */
    @GetMapping("/networth/{uId}")
    public ResponseEntity<List<NetWorth>> getNetWorthItems(@PathVariable("uId") int uId) {
        List<NetWorth> netWorthList = netWorthService.getNetWorthItemsByUserId(uId);
        return ResponseEntity.ok(netWorthList);
    }

    /**
     * Adds a new net worth item for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param netWorth - the net worth object received from the React form
     * @return - returns the saved net worth item as JSON
     */
    @PostMapping("/addNetWorthItem/{uId}")
    public ResponseEntity<NetWorth> addNetWorthItem(@PathVariable("uId") int uId,
                                                    @RequestBody NetWorth netWorth) {
        Optional<User> user = userRepository.findById(uId);
        netWorth.setUser(user.get());

        NetWorth saved = netWorthService.addNetWorthItem(netWorth);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a net worth item after the user requests removal
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the net worth id to be deleted
     * @return - returns a 200 OK response on success
     */
    @DeleteMapping("/deleteNetWorthItem/{uId}/{id}")
    public ResponseEntity<Void> deleteNetWorthItemById(@PathVariable(value = "uId") int uId,
                                                       @PathVariable(value = "id") int id) {
        netWorthService.deleteNetWorthItemById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a single net worth item by id - used for populating the update form
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the net worth id to be retrieved
     * @return - returns the net worth object as JSON
     */
    @GetMapping("/getNetWorthItem/{uId}/{id}")
    public ResponseEntity<NetWorth> getNetWorthItemById(@PathVariable(value = "uId") int uId,
                                                        @PathVariable(value = "id") int id) {
        NetWorth netWorth = netWorthService.getNetWorthItemById(id);
        return ResponseEntity.ok(netWorth);
    }

    /**
     * Updates an existing net worth item for the given user
     * @param uId - passing the userId via the URL to be used throughout this module
     * @param id - passing the net worth id to be updated
     * @param netWorth - the updated net worth object received from the React form
     * @return - returns the updated net worth item as JSON
     */
    @PutMapping("/updateNetWorthItem/{uId}/{id}")
    public ResponseEntity<NetWorth> updateNetWorthItem(@PathVariable(value = "uId") int uId,
                                                       @PathVariable(value = "id") int id,
                                                       @RequestBody NetWorth netWorth) {
        Optional<User> user = userRepository.findById(uId);
        netWorth.setNwId(id);
        netWorth.setUser(user.get());

        NetWorth updated = netWorthService.addNetWorthItem(netWorth);
        return ResponseEntity.ok(updated);
    }
}