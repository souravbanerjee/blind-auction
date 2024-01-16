package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "auction_user")
public class User {
    @Id
    private String userId;
    private String token;
    private String userName;
    private String userAddress;
    private String phoneNo;
    private String email;
}