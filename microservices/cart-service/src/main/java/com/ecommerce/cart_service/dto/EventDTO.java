package com.ecommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {

    private String eventType;
    private LocalDateTime timestamp;
    private String sourceService;
    private String topic;
    private Map<String, Object> payload;
    private Map<String, Object> snapshot;
}
