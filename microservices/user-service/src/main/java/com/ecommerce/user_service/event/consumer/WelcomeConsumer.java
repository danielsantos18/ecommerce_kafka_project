package com.ecommerce.user_service.event.consumer;

import com.ecommerce.user_service.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WelcomeConsumer {

    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    @KafkaListener(topics = "user-registered-topic", groupId = "welcome-group")
    public void handleUserRegisteredEvent(EventDTO event) {

        log.info("🎯 Incoming event: {}", event);

        Long userId = Long.valueOf(event.getSnapshot().get("userId").toString());
        Map<String, Object> payload = event.getPayload();

        System.out.println("Procesando flujo de bienvenida para userId: " + userId);

        String name = payload.get("name").toString();
        String email = payload.get("email").toString();

        // Crear el contenido del evento de bienvenida
        String subject = "¡Bienvenido a nuestra plataforma!";
        String content = "Hola " + name + ", gracias por registrarte en nuestra plataforma!.";

        Map<String, Object> welcomePayload = Map.of(
                "to", email,
                "subject", subject,
                "content", content
        );

        Map<String, Object> snapshot = Map.of(
                "userId", userId,
                "status", "PROCESSED",
                "welcomeEmailSent", true,
                "initialCredits", 100
        );

        // Usar EventDTO directamente
        EventDTO welcomeEvent = new EventDTO();
        welcomeEvent.setEventType("WELCOME_FLOW");
        welcomeEvent.setSourceService("user-service");
        welcomeEvent.setTopic("welcome-flow-topic");
        welcomeEvent.setTimestamp(LocalDateTime.now());
        welcomeEvent.setPayload(welcomePayload);
        welcomeEvent.setSnapshot(snapshot);

        try {
            kafkaTemplate.send("welcome-flow-topic", welcomeEvent);
            log.info("Publishing event to welcome-flow-topic: {}", welcomeEvent);
        } catch (Exception e) {
            System.out.println("welcome-flow failed to send");
        }
    }
}

