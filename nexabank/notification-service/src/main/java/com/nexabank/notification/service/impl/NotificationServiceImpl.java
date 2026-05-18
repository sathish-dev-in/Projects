package com.nexabank.notification.service.impl;

import com.nexabank.notification.annotation.AuditLog;
import com.nexabank.notification.document.Notification;
import com.nexabank.notification.dto.CreateNotificationRequest;
import com.nexabank.notification.dto.NotificationResponse;
import com.nexabank.notification.functional.NotificationHandler;
import com.nexabank.notification.repository.NotificationRepository;
import com.nexabank.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Notification service implementation demonstrating:
 * - Java 17 records (DTOs)
 * - CompletableFuture for async operations
 * - @Async for non-blocking MongoDB writes
 * - Functional interface (NotificationHandler) with lambda
 * - @AuditLog custom annotation with AOP
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    // Lambda assigned to functional interface — demonstrates @FunctionalInterface
    private final NotificationHandler<Notification> persistHandler =
            notification -> CompletableFuture.runAsync(() -> {
                repository.save(notification);
                log.info("[NOTIFICATION] Saved: {} for userId: {}",
                        notification.getType(), notification.getUserId());
            });

    // Chained handler using default method — demonstrates functional composition
    private final NotificationHandler<Notification> loggingHandler =
            notification -> CompletableFuture.runAsync(() ->
                    log.info("[NOTIFICATION EVENT] type={} | userId={} | title={}",
                            notification.getType(),
                            notification.getUserId(),
                            notification.getTitle())
            );

    @Override
    @Async("notificationExecutor")
    @AuditLog(action = "CREATE_NOTIFICATION", description = "Creating async notification")
    public CompletableFuture<Void> createAsync(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.userId())
                .type(request.type())
                .title(request.title())
                .message(request.message())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Use composed handler — demonstrates andThen() default method
        NotificationHandler<Notification> composedHandler = loggingHandler.andThen(persistHandler);
        return composedHandler.handle(notification);
    }

    @Override
    public List<NotificationResponse> getByUserId(String userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUnreadByUserId(String userId) {
        return repository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        repository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            repository.save(notification);
        });
    }

    @Override
    public long getUnreadCount(String userId) {
        return repository.countByUserIdAndReadFalse(userId);
    }

    private NotificationResponse mapToResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getUserId(),
                n.getType().name(),
                n.getTitle(),
                n.getMessage(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
