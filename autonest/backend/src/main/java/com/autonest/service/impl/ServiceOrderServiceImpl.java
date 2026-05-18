package com.autonest.service.impl;

import com.autonest.dto.request.ServiceOrderRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.mongo.ServiceReport;
import com.autonest.entity.postgres.Invoice;
import com.autonest.entity.postgres.Mechanic;
import com.autonest.entity.postgres.ServiceOrder;
import com.autonest.entity.postgres.Vehicle;
import com.autonest.enums.ServiceStatus;
import com.autonest.exception.BusinessException;
import com.autonest.exception.ResourceNotFoundException;
import com.autonest.repository.mongo.ServiceReportRepository;
import com.autonest.repository.postgres.InvoiceRepository;
import com.autonest.repository.postgres.MechanicRepository;
import com.autonest.repository.postgres.ServiceOrderRepository;
import com.autonest.repository.postgres.VehicleRepository;
import com.autonest.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service order implementation.
 * Demonstrates: ServiceStatus enum with canTransitionTo(), switch expressions,
 * Streams, Optional, and async MongoDB writes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final InvoiceRepository invoiceRepository;
    private final ServiceReportRepository serviceReportRepository;
    private final ActivityLogService activityLogService;

    @Override
    public GenericResponse<ServiceOrder> create(ServiceOrderRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", request.vehicleId()));

        ServiceOrder.ServiceOrderBuilder<?, ?> builder = ServiceOrder.builder()
                .vehicle(vehicle)
                .serviceType(request.serviceType())
                .description(request.description())
                .estimatedCost(request.estimatedCost())
                .notes(request.notes())
                .status(ServiceStatus.PENDING);

        if (request.mechanicId() != null) {
            Mechanic mechanic = mechanicRepository.findById(request.mechanicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mechanic", request.mechanicId()));
            builder.mechanic(mechanic);
        }

        ServiceOrder saved = serviceOrderRepository.save(builder.build());

        activityLogService.logAsync("ServiceOrder", saved.getId(), "CREATE",
                "Service order created for vehicle: " + vehicle.getDisplayName());

        return GenericResponse.success(generateMessage("Service Order", "created"), saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<ServiceOrder> findById(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));
        return GenericResponse.success(order);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<ServiceOrder>> findAll() {
        List<ServiceOrder> orders = serviceOrderRepository.findAll();
        return GenericResponse.success(buildListMessage("ServiceOrder", orders.size()), orders);
    }

    @Override
    public GenericResponse<ServiceOrder> update(Long id, ServiceOrderRequest request) {
        ServiceOrder existing = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));

        if (existing.getStatus().isTerminal()) {
            throw new BusinessException("Cannot update a " + existing.getStatus().getDisplayLabel() + " service order");
        }

        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", request.vehicleId()));

        existing.setVehicle(vehicle);
        existing.setServiceType(request.serviceType());
        existing.setDescription(request.description());
        existing.setEstimatedCost(request.estimatedCost());
        existing.setNotes(request.notes());

        if (request.mechanicId() != null) {
            Mechanic mechanic = mechanicRepository.findById(request.mechanicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mechanic", request.mechanicId()));
            existing.setMechanic(mechanic);
        }

        ServiceOrder updated = serviceOrderRepository.save(existing);

        activityLogService.logAsync("ServiceOrder", updated.getId(), "UPDATE",
                "Service order updated: " + updated.getOrderNumber());

        return GenericResponse.success(generateMessage("Service Order", "updated"), updated);
    }

    @Override
    public GenericResponse<Void> delete(Long id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));

        if (order.getStatus() == ServiceStatus.IN_PROGRESS) {
            throw new BusinessException("Cannot delete an in-progress service order");
        }

        serviceOrderRepository.delete(order);

        activityLogService.logAsync("ServiceOrder", id, "DELETE",
                "Service order deleted: " + order.getOrderNumber());

        return GenericResponse.successMessage(generateMessage("Service Order", "deleted"));
    }

    @Override
    public GenericResponse<ServiceOrder> updateStatus(Long id, ServiceStatus newStatus) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceOrder", id));

        ServiceStatus currentStatus = order.getStatus();

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw BusinessException.invalidTransition(currentStatus.name(), newStatus.name());
        }

        // Switch expression to handle status-specific side effects
        switch (newStatus) {
            case IN_PROGRESS -> order.setStartedAt(LocalDateTime.now());
            case COMPLETED -> {
                order.setCompletedAt(LocalDateTime.now());
                order.setActualCost(order.getEstimatedCost());
                generateInvoice(order);
                generateServiceReport(order);
            }
            case CANCELLED -> log.info("Service order {} cancelled", order.getOrderNumber());
            default -> log.debug("Status updated to {}", newStatus);
        }

        order.setStatus(newStatus);
        ServiceOrder updated = serviceOrderRepository.save(order);

        activityLogService.logAsync("ServiceOrder", updated.getId(), "STATUS_UPDATE",
                "Status changed: " + currentStatus + " -> " + newStatus + " for order " + updated.getOrderNumber());

        return GenericResponse.success("Status updated to " + newStatus.getDisplayLabel(), updated);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<ServiceOrder>> findByStatus(ServiceStatus status) {
        List<ServiceOrder> orders = serviceOrderRepository.findByStatus(status);
        return GenericResponse.success("Orders with status: " + status.getDisplayLabel(), orders);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<ServiceOrder>> findByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle", vehicleId);
        }
        List<ServiceOrder> orders = serviceOrderRepository.findByVehicleId(vehicleId);
        return GenericResponse.success("Orders for vehicle " + vehicleId, orders);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<ServiceOrder>> findActiveOrders() {
        List<ServiceOrder> orders = serviceOrderRepository.findActiveOrders();
        return GenericResponse.success("Active service orders", orders);
    }

    private void generateInvoice(ServiceOrder order) {
        BigDecimal subtotal = order.getActualCost() != null ? order.getActualCost() : order.getEstimatedCost();
        BigDecimal taxRate = BigDecimal.valueOf(0.18);
        BigDecimal taxAmount = subtotal.multiply(taxRate);
        BigDecimal totalAmount = subtotal.add(taxAmount);

        Invoice invoice = Invoice.builder()
                .serviceOrder(order)
                .invoiceNumber("INV-" + String.format("%06d", order.getId()))
                .subtotal(subtotal)
                .taxRate(taxRate)
                .taxAmount(taxAmount)
                .totalAmount(totalAmount)
                .build();

        invoiceRepository.save(invoice);
        log.info("Invoice generated: {} for order {}", invoice.getInvoiceNumber(), order.getOrderNumber());
    }

    private void generateServiceReport(ServiceOrder order) {
        ServiceReport report = ServiceReport.builder()
                .serviceOrderId(order.getId())
                .vehicleId(order.getVehicle().getId())
                .customerId(order.getVehicle().getCustomer().getId())
                .mechanicId(order.getMechanic() != null ? order.getMechanic().getId() : null)
                .vehicleInfo(order.getVehicle().getDisplayName())
                .customerName(order.getVehicle().getCustomer().getFullName())
                .mechanicName(order.getMechanic() != null ? order.getMechanic().getFullName() : "Unassigned")
                .serviceType(order.getServiceType().name())
                .description(order.getDescription())
                .totalCost(order.getActualCost())
                .status(order.getStatus().name())
                .startedAt(order.getStartedAt())
                .completedAt(order.getCompletedAt())
                .createdAt(LocalDateTime.now())
                .build();

        serviceReportRepository.save(report);
        log.info("Service report generated for order {}", order.getOrderNumber());
    }
}
