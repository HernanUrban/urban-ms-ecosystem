package com.urban.notificationservice.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class NotificationDeserializer extends JsonDeserializer<NotificationMessage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDeserializer.class);

  public NotificationDeserializer() {
    super(NotificationMessage.class);
  }

    @Override
    public NotificationMessage deserialize(String topic, byte[] data) {
      try {
        return super.deserialize(topic, data);
      } catch (Exception e) {
        LOGGER.error("Problem deserializing data " + new String(data) + " on topic " + topic, e);
        return null;
      }
    }

}
