package com.sourav.blindauction.repository;

import com.sourav.blindauction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByToken(String token);
}