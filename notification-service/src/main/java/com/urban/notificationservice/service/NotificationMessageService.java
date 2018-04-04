package com.urban.notificationservice.service;

import com.urban.notificationservice.domain.NotificationMessage;
import com.urban.notificationservice.repo.NotificationMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationMessageService {

    @Autowired
    private NotificationMessageRepository repo;

    public void save(NotificationMessage message) {
        repo.save(message);
    }

    public List<NotificationMessage> getNotificationsByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    public List<NotificationMessage> getNotificationsByDate(Date from, Date to) {
        return repo.findByCreationDateBetween(from, to);
    }

    public List<NotificationMessage> getNotificationsByEmail(String email) {
        return repo.findByEmail(email);
    }
}
