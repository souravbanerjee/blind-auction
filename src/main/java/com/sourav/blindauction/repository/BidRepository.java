package com.sourav.blindauction.repository;

import com.sourav.blindauction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, String> {
    Bid findTopByProductIdOrderByBidAmountDesc(String productId);
    // You can add custom query methods if needed
}