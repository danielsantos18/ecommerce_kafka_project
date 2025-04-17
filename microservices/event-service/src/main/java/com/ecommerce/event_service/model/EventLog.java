package com.ecommerce.event_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "event_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLog {
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String eventType;
    private String source;
    private String topic;
    private Map<String, Object> payload;
    private Map<String, Object> snapshot;
}
