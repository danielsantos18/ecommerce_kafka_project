package com.ecommerce.user_service.event.producer;

import com.ecommerce.user_service.dto.EventDTO;
import com.ecommerce.user_service.event.UserDeletedEvent;
import com.ecommerce.user_service.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, EventDTO> kafkaTemplate;

    public void sendUserRegisteredEvent(UserRegisteredEvent userEvent) {
        Map<String, Object> payload = Map.of(
                "name", userEvent.getName(),
                "lastname", userEvent.getLastname(),
                "phone", userEvent.getPhone(),
                "email", userEvent.getEmail()
                // ⚠️ Evita enviar la contraseña a menos que esté encriptada y sea necesario
        );

        Map<String, Object> snapshot = Map.of(
                "userId", userEvent.getId(),
                "status", "REGISTERED"
        );

        EventDTO event = new EventDTO();
        event.setEventType("USER_REGISTRATION");
        event.setSourceService("user-service");
        event.setTopic("user-registered-topic");
        event.setTimestamp(LocalDateTime.now());
        event.setPayload(payload);
        event.setSnapshot(snapshot);

        try {
            kafkaTemplate.send("user-registered-topic", event);
            System.out.println("UserRegisteredEvent sent to user-registered-topic");
        } catch (Exception e) {
            System.out.println("UserRegisteredEvent failed to send");
        }
    }

    public void sendUserDeletedEvent(UserDeletedEvent userEvent) {
        EventDTO event = new EventDTO();
        event.setEventType("USER_REGISTRATION");
        event.setSourceService("user-service");
        event.setTimestamp(LocalDateTime.now());

        try {
            kafkaTemplate.send("user-deleted-topic", event);
            System.out.println("UserDeletedEvent sent to user-deleted");
        } catch (Exception e) {
            System.out.println("UserDeletedEvent failed to send to user-deleted");
        }
    }
}
