package com.autonest.entity.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ServiceReport MongoDB document — rich report for each completed service.
 */
@Document(collection = "service_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReport {

    @Id
    private String id;

    @Field("service_order_id")
    private Long serviceOrderId;

    @Field("vehicle_id")
    private Long vehicleId;

    @Field("customer_id")
    private Long customerId;

    @Field("mechanic_id")
    private Long mechanicId;

    @Field("vehicle_info")
    private String vehicleInfo;

    @Field("customer_name")
    private String customerName;

    @Field("mechanic_name")
    private String mechanicName;

    @Field("service_type")
    private String serviceType;

    @Field("description")
    private String description;

    @Field("work_performed")
    private List<String> workPerformed;

    @Field("parts_used")
    private List<PartUsed> partsUsed;

    @Field("total_cost")
    private BigDecimal totalCost;

    @Field("status")
    private String status;

    @Field("started_at")
    private LocalDateTime startedAt;

    @Field("completed_at")
    private LocalDateTime completedAt;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartUsed {
        private String partNumber;
        private String partName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;
    }
}
