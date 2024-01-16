package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "productId")
    private String id;
    private String sellerId;
    private String productName;
    private double minBidPrice;
    private boolean auctionClosed;

}
