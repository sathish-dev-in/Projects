package com.autonest.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * ActivityLog MongoDB document — audit trail for every mutation in the system.
 * Written asynchronously via CompletableFuture for non-blocking operation.
 */
@Document(collection = "activity_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Id
    private String id;

    @Field("entity_type")
    private String entityType;

    @Field("entity_id")
    private Long entityId;

    @Field("action")
    private String action;

    @Field("description")
    private String description;

    @Field("performed_by")
    private String performedBy;

    @Field("ip_address")
    private String ipAddress;

    @Field("old_values")
    private Map<String, Object> oldValues;

    @Field("new_values")
    private Map<String, Object> newValues;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("success")
    private boolean success;

    public static ActivityLog of(String entityType, Long entityId, String action, String description) {
        return ActivityLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .description(description)
                .performedBy("system")
                .timestamp(LocalDateTime.now())
                .success(true)
                .build();
    }
}
