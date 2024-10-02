package com.electronicstore.repositories;

import com.electronicstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {

    List<Product> findByTitleContaining(String title);
    List<Product> findByLiveTrue();
}
