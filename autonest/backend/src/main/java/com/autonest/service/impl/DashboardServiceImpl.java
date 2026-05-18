package com.autonest.service.impl;

import com.autonest.dto.response.DashboardStatsDTO;
import com.autonest.dto.response.GenericResponse;
import com.autonest.enums.ServiceStatus;
import com.autonest.repository.postgres.*;
import com.autonest.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Dashboard service — aggregates statistics from multiple repositories.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final PartRepository partRepository;
    private final MechanicRepository mechanicRepository;

    @Override
    public GenericResponse<DashboardStatsDTO> getStats() {
        BigDecimal totalRevenue = serviceOrderRepository.sumRevenueFromCompleted();

        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .totalCustomers(customerRepository.countByActiveTrue())
                .totalVehicles(vehicleRepository.countByActiveTrue())
                .activeServices(serviceOrderRepository.countByStatus(ServiceStatus.IN_PROGRESS))
                .pendingServices(serviceOrderRepository.countByStatus(ServiceStatus.PENDING))
                .completedServices(serviceOrderRepository.countByStatus(ServiceStatus.COMPLETED))
                .cancelledServices(serviceOrderRepository.countByStatus(ServiceStatus.CANCELLED))
                .totalParts(partRepository.count())
                .lowStockParts(partRepository.countLowStockParts())
                .totalRevenue(totalRevenue)
                .totalMechanics(mechanicRepository.countByActiveTrue())
                .build();

        return GenericResponse.success("Dashboard statistics retrieved", stats);
    }
}
