package com.nexabank.notification.service;

import com.nexabank.notification.dto.CreateNotificationRequest;
import com.nexabank.notification.dto.NotificationResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Notification service interface.
 */
public interface NotificationService {

    CompletableFuture<Void> createAsync(CreateNotificationRequest request);

    List<NotificationResponse> getByUserId(String userId);

    List<NotificationResponse> getUnreadByUserId(String userId);

    void markAsRead(String notificationId);

    long getUnreadCount(String userId);
}
