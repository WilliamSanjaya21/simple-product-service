package com.learn.simple_product_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.simple_product_service.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>{
	
	Optional<Product> findBySku(String sku);
}
