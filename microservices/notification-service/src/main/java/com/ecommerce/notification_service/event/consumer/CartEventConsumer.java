package com.ecommerce.notification_service.event.consumer;

import com.ecommerce.notification_service.dto.EventDTO;
import com.ecommerce.notification_service.model.Notification;
import com.ecommerce.notification_service.repo.INotificationRepo;
import com.ecommerce.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartEventConsumer {

    private final EmailService emailService;
    private final INotificationRepo repository;
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    @KafkaListener(topics = "cart-updates", groupId = "notification-group")
    public void consumeCartEvent(EventDTO event) {
        try {
            log.info("🛒 Evento de carrito recibido: {}", event);

            Map<String, Object> payload = event.getPayload();

            String to = payload.get("to").toString();
            String userName = payload.get("name").toString();

            String subject = "Has añadido un producto a tu carrito 🛍️";
            String content = "Hola " + userName + ", acabas de añadir un producto a tu carrito. ¡No lo olvides!";

            // Enviar correo
            emailService.sendEmail(to, subject, content);

            // Guardar notificación
            repository.save(Notification.builder()
                    .userId(Long.valueOf(event.getPayload().get("userId").toString()))
                    .Email(to)
                    .subject(subject)
                    .message(content)
                    .sentAt(LocalDateTime.now())
                    .build());

            log.info("Notificación de carrito enviada a {}", to);

            // Publicar evento NOTIFICATION_SENT
            EventDTO notificationEvent = new EventDTO();
            notificationEvent.setEventType("NOTIFICATION_SENT");
            notificationEvent.setSourceService("notification-service");
            notificationEvent.setTopic("notification-topic");
            notificationEvent.setTimestamp(LocalDateTime.now());
            notificationEvent.setPayload(Map.of(
                    "to", to,
                    "subject", subject,
                    "content", content
            ));
            notificationEvent.setSnapshot(Map.of(
                    "status", "DELIVERED"
            ));

            kafkaTemplate.send("notification-topic", notificationEvent);
            log.info("Evento NOTIFICATION_SENT publicado desde carrito");

        } catch (Exception e) {
            log.error("❌ Error procesando evento desde cart-topic", e);
        }
    }
}
