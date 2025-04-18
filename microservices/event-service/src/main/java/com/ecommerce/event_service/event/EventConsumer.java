package com.ecommerce.event_service.event;

import com.ecommerce.event_service.dto.EventDTO;
import com.ecommerce.event_service.model.EventLog;
import com.ecommerce.event_service.repo.IEventLogRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final IEventLogRepo eventRepository;

    @KafkaListener(topics = {"user-registered-topic", "cart-updates", "welcome-flow-topic", "notification-topic"}, groupId = "event-group")
    public void consumeEvent(EventDTO event) {

        log.info("🎯 Incoming event: {}", event);
        //log.info("Received event from topic {}: {}", event.getTopic(), event);

        Map<String, Object> payload = event.getPayload();
        Map<String, Object> snapshot = event.getSnapshot();

        String eventId = "evt_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        EventLog eventLog = new EventLog();
        eventLog.setId(eventId);
        eventLog.setTimestamp(event.getTimestamp());
        eventLog.setEventType(event.getEventType());
        eventLog.setSource(event.getSourceService());
        eventLog.setTopic(event.getTopic());
        eventLog.setPayload(payload);
        eventLog.setSnapshot(snapshot);

        eventRepository.save(eventLog);
        log.info("✅ Event saved in Mongo: {}", eventLog);

    }
}