package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.model.Cart;
import com.ecommerce.cart_service.repo.ICartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final ICartRepo cartItemRepository;

    // Ya lo tenías:
    @PostMapping("/items")
    public Cart addItem(@RequestBody Cart item) {
        return cartItemRepository.save(item);
    }

    // Nuevo: obtener los ítems de un usuario
    @GetMapping("/user/{userId}")
    public List<Cart> getCartByUser(@PathVariable String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        if (cartItemRepository.existsById(id)) {
            cartItemRepository.deleteById(id);
            return ResponseEntity.ok("Item eliminado del carrito.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteCartByUser(@PathVariable String userId) {
        List<Cart> items = cartItemRepository.findByUserId(userId);
        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            cartItemRepository.deleteAll(items);
            return ResponseEntity.ok("Carrito vaciado para el usuario: " + userId);
        }
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable Long id,
            @RequestBody Cart updatedItem) {
        return cartItemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setQuantity(updatedItem.getQuantity());
                    Cart saved = cartItemRepository.save(existingItem);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
