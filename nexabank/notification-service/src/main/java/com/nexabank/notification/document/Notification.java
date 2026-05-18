package com.nexabank.notification.document;

import com.nexabank.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * MongoDB document for notification storage.
 * Async writes demonstrate CompletableFuture usage.
 */
@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    @Indexed
    @Field("user_id")
    private String userId;

    private NotificationType type;

    private String title;

    private String message;

    @Field("is_read")
    private boolean read;

    @Field("created_at")
    private LocalDateTime createdAt;
}
