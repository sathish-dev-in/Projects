package com.autonest.controller;

import com.autonest.annotation.Loggable;
import com.autonest.dto.request.ServiceOrderRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.ServiceOrder;
import com.autonest.enums.ServiceStatus;
import com.autonest.service.ServiceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
@Validated
@Loggable
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @PostMapping
    @Loggable(value = "createServiceOrder", logArgs = true)
    public ResponseEntity<GenericResponse<ServiceOrder>> create(@Valid @RequestBody ServiceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOrderService.create(request));
    }

    @GetMapping("/{id}")
    @Loggable(value = "getServiceOrder")
    public ResponseEntity<GenericResponse<ServiceOrder>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderService.findById(id));
    }

    @GetMapping
    @Loggable(value = "getAllServiceOrders", logArgs = false)
    public ResponseEntity<GenericResponse<List<ServiceOrder>>> findAll() {
        return ResponseEntity.ok(serviceOrderService.findAll());
    }

    @PutMapping("/{id}")
    @Loggable(value = "updateServiceOrder")
    public ResponseEntity<GenericResponse<ServiceOrder>> update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceOrderRequest request) {
        return ResponseEntity.ok(serviceOrderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Loggable(value = "deleteServiceOrder")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOrderService.delete(id));
    }

    @PatchMapping("/{id}/status")
    @Loggable(value = "updateOrderStatus")
    public ResponseEntity<GenericResponse<ServiceOrder>> updateStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status) {
        return ResponseEntity.ok(serviceOrderService.updateStatus(id, status));
    }

    @GetMapping("/status/{status}")
    @Loggable(value = "getOrdersByStatus")
    public ResponseEntity<GenericResponse<List<ServiceOrder>>> findByStatus(@PathVariable ServiceStatus status) {
        return ResponseEntity.ok(serviceOrderService.findByStatus(status));
    }

    @GetMapping("/active")
    @Loggable(value = "getActiveOrders", logArgs = false)
    public ResponseEntity<GenericResponse<List<ServiceOrder>>> findActive() {
        return ResponseEntity.ok(serviceOrderService.findActiveOrders());
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Loggable(value = "getOrdersByVehicle")
    public ResponseEntity<GenericResponse<List<ServiceOrder>>> findByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(serviceOrderService.findByVehicleId(vehicleId));
    }
}
