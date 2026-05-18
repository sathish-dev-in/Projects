package com.autonest.repository.mongo;

import com.autonest.entity.mongo.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {

    List<ActivityLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<ActivityLog> findByEntityType(String entityType);

    List<ActivityLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

    List<ActivityLog> findTop50ByOrderByTimestampDesc();
}
