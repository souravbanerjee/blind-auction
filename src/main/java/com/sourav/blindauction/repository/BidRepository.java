package com.sourav.blindauction.repository;

import com.sourav.blindauction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, String> {
    Bid findTopByProductIdOrderByBidAmountDesc(String productId);
    // more custom queries can be added
}