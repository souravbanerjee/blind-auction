package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Product {
    @Id
    private String productId;
    private String sellerId;
    private String productName;
    private double minBidPrice;
    private boolean auctionClosed;

}
