package com.nexabank.notification.functional;

import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for notification handling.
 * Demonstrates Java 17 @FunctionalInterface with default methods and CompletableFuture.
 *
 * @param <T> the notification type
 */
@FunctionalInterface
public interface NotificationHandler<T> {

    /**
     * Handle a notification asynchronously.
     *
     * @param notification the notification to handle
     * @return a CompletableFuture that completes when handling is done
     */
    CompletableFuture<Void> handle(T notification);

    /**
     * Default method for chaining notification handlers.
     * Demonstrates functional composition with default interface methods.
     */
    default NotificationHandler<T> andThen(NotificationHandler<T> after) {
        return notification -> this.handle(notification)
                .thenCompose(v -> after.handle(notification));
    }

    /**
     * Default method to handle notification and ignore errors silently.
     */
    default NotificationHandler<T> withFallback(NotificationHandler<T> fallback) {
        return notification -> this.handle(notification)
                .exceptionallyCompose(ex -> fallback.handle(notification));
    }
}
