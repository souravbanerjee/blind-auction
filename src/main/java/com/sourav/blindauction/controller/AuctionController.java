package com.sourav.blindauction.controller;

import com.sourav.blindauction.model.Bid;
import com.sourav.blindauction.model.Product;
import com.sourav.blindauction.model.User;
import com.sourav.blindauction.service.AuctionService;
import com.sourav.blindauction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuctionService auctionService;

    @PostMapping("/users/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping("/users/validate-token")
    public boolean validateToken(@RequestParam String token) {
        return userService.validateToken(token);
    }

    @PostMapping("/products/register")
    public String registerProduct(@RequestHeader("Authorization") String sellerToken,
                                  @RequestBody Product product) {
        return auctionService.registerProduct(sellerToken, product);
    }

    @PostMapping("/bid")
    public boolean placeBid(@RequestHeader("Authorization") String buyerToken,
                            @RequestParam String productId, @RequestBody Bid bid) {
        return auctionService.placeBid(buyerToken, productId, bid);
    }

    @PostMapping("/close")
    public String closeAuction(@RequestHeader("Authorization") String sellerToken,
                               @RequestParam String productId) {
        return auctionService.closeAuction(sellerToken, productId);
    }
}
