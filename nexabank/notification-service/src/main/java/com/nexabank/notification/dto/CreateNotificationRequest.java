package com.nexabank.notification.dto;

import com.nexabank.notification.enums.NotificationType;

/**
 * Request DTO for creating notifications.
 * Java 17 record.
 */
public record CreateNotificationRequest(
        String userId,
        NotificationType type,
        String title,
        String message
) {}
