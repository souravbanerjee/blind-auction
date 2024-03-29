package com.sourav.blindauction.service;

import com.sourav.blindauction.model.User;
import com.sourav.blindauction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_REGISTERED = "User registered successfully: ";
    @Autowired
    private UserRepository userRepository;

    @Override
    public String registerUser(User user) {
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);
        return USER_REGISTERED+token;
    }

    @Override
    public boolean validateToken(String token) {
        User user = userRepository.findByToken(token);
        return user != null;
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByToken(token);
    }
}
