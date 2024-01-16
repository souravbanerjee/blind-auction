package com.sourav.blindauction.service;

import com.sourav.blindauction.model.Bid;
import com.sourav.blindauction.model.Product;

public interface AuctionService {
    String registerProduct(String sellerToken, Product product);
    boolean placeBid(String buyerToken, String productId, Bid bid);
    String closeAuction(String sellerToken, String productId);
}