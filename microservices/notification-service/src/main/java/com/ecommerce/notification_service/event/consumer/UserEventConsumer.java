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
public class UserEventConsumer {

    private final EmailService emailService;
    private final INotificationRepo repository;
    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    @KafkaListener(topics = "welcome-flow-topic", groupId = "notification-group")
    public void consume(EventDTO event) {
        try {
            log.info("🎯 Incoming event: {}", event);

            Map<String, Object> payload = event.getPayload();

            String to = payload.get("to").toString();
            String subject = payload.get("subject").toString();
            String content = payload.get("content").toString();

            emailService.sendEmail(to, subject, content);

            repository.save(Notification.builder()
                    .userId(Long.valueOf(event.getSnapshot().get("userId").toString()))
                    .Email(to)
                    .subject(subject)
                    .message(content)
                    .sentAt(LocalDateTime.now())
                    .build());

            log.info("Notificación enviada y registrada para: {}", to);

            // Crear nuevo evento para notificación enviada
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
                    //"userId", event.getSnapshot().get("userId"),
                    "status", "DELIVERED"
            ));

            // Enviar evento
            kafkaTemplate.send("notification-topic", notificationEvent);
            log.info("Evento NOTIFICATION_SENT publicado en notification-topic");


        } catch (Exception e) {
            log.error("Error procesando evento desde welcome-flow-topic", e);
        }
    }

}