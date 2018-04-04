package com.urban.notificationservice.service;

import com.urban.notificationservice.domain.NotificationMessage;
import com.urban.notificationservice.domain.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class NotificationMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessageListener.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private MailService mailService;

    @Autowired
    private NotificationMessageService messageService;

    @Autowired
    private Environment env;

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "${kafka.consumer.topic}")
    public void receive(NotificationMessage event) {
        if (event != null) {
            LOGGER.info("received payload='{}'", event.toString());
            try {
                messageService.save(event);
            } catch (Exception e) {
                LOGGER.error(String.format("Error storing event='{%s}'", event.toString()), e);
            }
            NotificationType type = NotificationType.fromValue(event.getNotificationType());
            List<String> templates = env.getProperty("notification.template.mapping." + type, List.class);
            if (templates != null) {
                templates.stream().forEach(t -> {
                    String subject = env.getProperty("notification.subject.mapping." + t);
                    mailService.prepareAndSend(event.getEmail(), subject, t, event.getPayload());
                });
            }
        }
        latch.countDown();
    }

}
