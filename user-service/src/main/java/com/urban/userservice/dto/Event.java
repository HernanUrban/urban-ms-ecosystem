package com.urban.userservice.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event implements Serializable {

  private String eventId;
  private String userId;
  private String resourceId;
  private Date creationDate;
  private String notificationType;
  private String email;
  private Map<String, String> payload;

  public Event(){
    this.eventId = UUID.randomUUID().toString();
  }

  public String getEventId() {
    return eventId;
  }

  public String getUserId() {
    return userId;
  }

  public Event setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public String getResourceId() {
    return resourceId;
  }

  public Event setResourceId(String resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public Event setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  public String getNotificationType() {
    return notificationType;
  }

  public Event setNotificationType(String notificationType) {
    this.notificationType = notificationType;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Event setEmail(String email) {
    this.email = email;
    return this;
  }

  public Map<String, String> getPayload() {
    return payload;
  }

  public Event addAttribute(String attribute, String value) {
    if (this.payload == null) {
      payload = new HashMap<String, String>();
    }
    payload.put(attribute, value);
    return this;
  }
}
