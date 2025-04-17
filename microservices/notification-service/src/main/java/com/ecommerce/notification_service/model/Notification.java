package com.ecommerce.notification_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    private String id;
    private Long userId;
    private String Email;
    private String subject;
    private String message;
    private LocalDateTime sentAt;
}
