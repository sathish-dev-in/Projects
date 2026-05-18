package com.nexabank.notification.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for notification data.
 * Java 17 record.
 */
public record NotificationResponse(
        String id,
        String userId,
        String type,
        String title,
        String message,
        boolean read,
        LocalDateTime createdAt
) {}
