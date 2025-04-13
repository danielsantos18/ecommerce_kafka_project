package com.ecommerce.user_service.util;

import com.ecommerce.user_service.event.UserDeletedEvent;
import com.ecommerce.user_service.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            kafkaTemplate.send("user-registration", event);
            System.out.println("UserRegisteredEvent sent to user-registration");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("UserRegisteredEvent failed to send to user-registration");
        }
    }

    public void sendUserDeletedEvent(UserDeletedEvent event) {
        try {
            kafkaTemplate.send("user-deleted", event);
            System.out.println("UserDeletedEvent sent to user-deleted");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("UserDeletedEvent failed to send to user-deleted");
        }
    }
}
