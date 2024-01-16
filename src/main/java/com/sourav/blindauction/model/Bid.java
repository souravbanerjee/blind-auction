package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Bid {
    @Id
    @Column(name = "bidId")
    private String id;
    private String productId;
    private String buyerId;
    private double bidAmount;

}