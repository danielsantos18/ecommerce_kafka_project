package com.ecommerce.product_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.product_service.model.Product;

@Repository
public interface IProductRepo extends JpaRepository<Product, Long> {
}
