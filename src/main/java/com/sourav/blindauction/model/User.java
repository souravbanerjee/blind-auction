package com.sourav.blindauction.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "auction_user")
public class User {
    @Id
    @Column(name = "userId")
    private String id;
    private String token;
    private String userName;
    private String userAddress;
    private String phoneNo;
    private String email;
}