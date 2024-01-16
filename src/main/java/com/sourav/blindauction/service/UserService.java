package com.sourav.blindauction.service;

import com.sourav.blindauction.model.User;

public interface UserService {
    String registerUser(User user);
    boolean validateToken(String token);
    User getUserByToken(String token);
}