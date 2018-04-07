package com.urban.notificationservice.repo;

import com.urban.notificationservice.domain.NotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface NotificationMessageRepository extends MongoRepository<NotificationMessage, String> {

    List<NotificationMessage> findByUserId(String userId);

    List<NotificationMessage> findByEmail(String email);

    List<NotificationMessage> findByCreationDateBetween(Date from, Date to);
}