package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.EventDTO;
import com.ecommerce.cart_service.dto.UserApiResponse;
import com.ecommerce.cart_service.model.Cart;
import com.ecommerce.cart_service.repo.ICartRepo;
import com.ecommerce.cart_service.util.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ICartRepo repository;
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;
    private final UserClient userClient;

    private static final String TOPIC_ADD = "cart-updates";

    public Cart addItem(Cart item) {
        // Buscar si ya existe el ítem en el carrito
        var existing = repository.findByUserIdAndProductId(item.getUserId(), item.getProductId());
        if (existing.isPresent()) {
            var found = existing.get();
            found.setQuantity(found.getQuantity() + item.getQuantity());
            item = found;
        }

        Cart saved = repository.save(item);

        // Obtener información del usuario desde el user-service
        UserApiResponse response = userClient.getUserById(saved.getUserId()); // Llamada HTTP al user-service

        // Verificar que la respuesta es exitosa y obtener los datos del usuario
        if (response != null && response.getData() != null) {
            UserApiResponse.UserResponse user = response.getData();  // Acceder a los datos del usuario

            String email = user.getEmail();
            String name = user.getName();

            // Construir payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", saved.getUserId());
            payload.put("productId", saved.getProductId());
            payload.put("quantity", saved.getQuantity());
            payload.put("to", email);  // Correo electrónico
            payload.put("name", name); // Nombre del usuario

            // Construir snapshot
            Map<String, Object> snapshot = new HashMap<>();
            snapshot.put("cartId", "cart_" + saved.getUserId());
            snapshot.put("totalItems", repository.findByUserId(saved.getUserId()).size());
            snapshot.put("updatedAt", Instant.now().toString());

            // Crear el EventDTO
            EventDTO event = EventDTO.builder()
                    .eventType("ITEM_ADDED")
                    .timestamp(LocalDateTime.now())
                    .sourceService("cart-service")
                    .topic(TOPIC_ADD)
                    .payload(payload)
                    .snapshot(snapshot)
                    .build();

            // Enviar el evento a Kafka
            kafkaTemplate.send(TOPIC_ADD, event);
        }

        return saved;
    }
}
