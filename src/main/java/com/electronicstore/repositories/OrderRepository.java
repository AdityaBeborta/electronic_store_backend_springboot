package com.electronicstore.repositories;

import com.electronicstore.entities.Order;
import com.electronicstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    List<Order> findByUser(User user);
}
