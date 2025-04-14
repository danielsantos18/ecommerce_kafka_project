package com.ecommerce.notification_service.util;

import com.ecommerce.notification_service.model.Notification;
import com.ecommerce.notification_service.repo.INotificationRepo;
import com.ecommerce.notification_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventConsumer {

    private final EmailService emailService;
    private final INotificationRepo repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-registration", groupId = "notification-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> data = objectMapper.readValue(record.value(), Map.class);
            Long userId = Long.valueOf(data.get("id").toString());
            String email = data.get("email").toString();
            String name = data.get("name").toString();

            String msg = "¡Hola " + name + "! Bienvenido a nuestra plataforma.";
            emailService.sendEmail(email, "Registro exitoso", msg);

            repository.save(Notification.builder()
                    .userId(userId)
                    .userEmail(email)
                    .message(msg)
                    .sentAt(LocalDateTime.now())
                    .build());

            log.info("Notificación enviada y guardada para el usuario: {}", email);

        } catch (Exception e) {
            log.error("Error procesando el mensaje de Kafka", e);
        }
    }
}
