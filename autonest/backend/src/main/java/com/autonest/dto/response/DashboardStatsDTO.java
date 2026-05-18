package com.autonest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Dashboard statistics aggregate DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private long totalCustomers;
    private long totalVehicles;
    private long activeServices;
    private long pendingServices;
    private long completedServices;
    private long cancelledServices;
    private long totalParts;
    private long lowStockParts;
    private BigDecimal totalRevenue;
    private long totalMechanics;
}
