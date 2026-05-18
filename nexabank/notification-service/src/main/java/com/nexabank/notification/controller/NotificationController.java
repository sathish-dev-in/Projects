package com.nexabank.notification.controller;

import com.nexabank.notification.dto.CreateNotificationRequest;
import com.nexabank.notification.dto.NotificationResponse;
import com.nexabank.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for notification management.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createNotification(
            @RequestBody CreateNotificationRequest request) {
        CompletableFuture<Void> future = notificationService.createAsync(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("status", "queued", "message", "Notification queued for async delivery"));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestHeader("X-User-Id") String userId) {
        List<NotificationResponse> notifications = notificationService.getByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(
            @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.getUnreadByUserId(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @RequestHeader("X-User-Id") String userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Notification marked as read"));
    }
}
