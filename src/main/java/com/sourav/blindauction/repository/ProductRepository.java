package com.sourav.blindauction.repository;

import com.sourav.blindauction.model.Product;
import com.sourav.blindauction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}