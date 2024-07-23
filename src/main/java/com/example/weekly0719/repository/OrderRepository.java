package com.example.weekly0719.repository;

import com.example.weekly0719.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
