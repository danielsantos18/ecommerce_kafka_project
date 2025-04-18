package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.EventDTO;
import com.ecommerce.cart_service.model.Cart;
import com.ecommerce.cart_service.repo.ICartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ICartRepo repository;
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    private static final String TOPIC_ADD = "cart-updates";

    public Cart addItem(Cart item) {
        // Buscar si ya existe
        var existing = repository.findByUserIdAndProductId(item.getUserId(), item.getProductId());
        if (existing.isPresent()) {
            var found = existing.get();
            found.setQuantity(found.getQuantity() + item.getQuantity());
            item = found;
        }

        Cart saved = repository.save(item);

        // Construir payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", saved.getUserId());
        payload.put("productId", saved.getProductId());
        payload.put("quantity", saved.getQuantity());

        // Construir snapshot
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("cartId", "cart_" + saved.getUserId());
        snapshot.put("totalItems", repository.findByUserId(saved.getUserId()).size());
        snapshot.put("updatedAt", Instant.now().toString());

        // Crear EventDTO
        EventDTO event = EventDTO.builder()
                .eventType("ITEM_ADDED")
                .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                .sourceService("CartService")
                .topic(TOPIC_ADD)
                .payload(payload)
                .snapshot(snapshot)
                .build();

        kafkaTemplate.send(TOPIC_ADD, event);

        return saved;
    }
}
