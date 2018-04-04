package com.urban.notificationservice.domain;

import org.springframework.util.StringUtils;

public enum NotificationType {
    CREATE_USER("create_user"),
    UPDATE_USER("update_user"),
    EMAIL_CHANGE("email_change"),
    UNKNOWN("unknown");

    private String notificationName;

    private NotificationType(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public static NotificationType fromValue(String name) {
        if (!StringUtils.isEmpty(name)) {
            for (NotificationType value : NotificationType.values()) {
                if (value.getNotificationName().equalsIgnoreCase(name)) {
                    return value;
                }
            }
        }
        return NotificationType.UNKNOWN;
    }


}
