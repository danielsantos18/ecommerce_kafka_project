package com.ecommerce.cart_service.repo;

import com.ecommerce.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICartRepo extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(String userId);

    Optional<Cart> findByUserIdAndProductId(String userId, String productId);

    void deleteByUserIdAndProductId(String userId, String productId);
}
