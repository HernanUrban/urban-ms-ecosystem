package com.urban.userservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urban.userservice.dto.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class EventPublisher {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

  @Value("${spring.kafka.producer.topic}")
  private String topic;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  /**
   * Sends a message asynchronously through Kafka stream. This method
   * retrieves a future once the message is published and log the result.
   *
   * @param messageDTO
   *            the message to send
   */
  public void send(Event messageDTO) {

    try {
      String message = mapper.writeValueAsString(messageDTO);

      ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

      future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
        @Override
        public void onSuccess(SendResult<String, String> result) {
          LOGGER.info("sent message='{}' with offset={}", message, result.getRecordMetadata().offset());

        }

        @Override
        public void onFailure(Throwable ex) {
          LOGGER.warn("Unable to deliver message='{}' cause '{}'", message, ex.getMessage());
        }
      });
    } catch (Exception e) {
      LOGGER.error("Unable to send message event", e);
    }
  }
}
