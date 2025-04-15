package com.ecommerce.notification_service.repo;

import com.ecommerce.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface INotificationRepo extends MongoRepository<Notification, String> {
}
