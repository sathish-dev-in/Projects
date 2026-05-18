package com.autonest.service.impl;

import com.autonest.entity.mongo.ActivityLog;
import com.autonest.repository.mongo.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for writing activity logs asynchronously to MongoDB.
 * Demonstrates CompletableFuture fire-and-forget async pattern.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    /**
     * Logs an activity asynchronously — fire-and-forget.
     * Uses CompletableFuture.runAsync() to avoid blocking the caller.
     */
    public void logAsync(String entityType, Long entityId, String action, String description) {
        CompletableFuture.runAsync(() -> {
            try {
                ActivityLog logEntry = ActivityLog.of(entityType, entityId, action, description);
                activityLogRepository.save(logEntry);
                log.debug("[ActivityLog] Saved: {} {} {} - {}", action, entityType, entityId, description);
            } catch (Exception ex) {
                log.warn("[ActivityLog] Failed to save activity log: {}", ex.getMessage());
            }
        });
    }

    /**
     * Logs with explicit builder control — also fire-and-forget.
     */
    public void logAsync(ActivityLog activityLog) {
        CompletableFuture.runAsync(() -> {
            try {
                activityLogRepository.save(activityLog);
            } catch (Exception ex) {
                log.warn("[ActivityLog] Failed to persist log: {}", ex.getMessage());
            }
        });
    }
}
