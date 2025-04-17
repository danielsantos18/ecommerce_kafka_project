package com.ecommerce.event_service.repo;

import com.ecommerce.event_service.model.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IEventLogRepo extends MongoRepository<EventLog, String> {
}
