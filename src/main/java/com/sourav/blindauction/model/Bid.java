package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Bid {
    @Id
    private String bidId;
    private String productId;
    private String buyerId;
    private double bidAmount;

}