package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.model.Cart;
import com.ecommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/item")
    public ResponseEntity<Cart> addItem(@RequestBody Cart item) {
        Cart saved = cartService.addItem(item);
        return ResponseEntity.ok(saved);
    }
}
