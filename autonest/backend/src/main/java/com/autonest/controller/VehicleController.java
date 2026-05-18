package com.autonest.controller;

import com.autonest.annotation.Loggable;
import com.autonest.dto.request.VehicleRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Vehicle;
import com.autonest.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Validated
@Loggable
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @Loggable(value = "registerVehicle", logArgs = true)
    public ResponseEntity<GenericResponse<Vehicle>> create(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(request));
    }

    @GetMapping("/{id}")
    @Loggable(value = "getVehicle")
    public ResponseEntity<GenericResponse<Vehicle>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @GetMapping
    @Loggable(value = "getAllVehicles", logArgs = false)
    public ResponseEntity<GenericResponse<List<Vehicle>>> findAll() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PutMapping("/{id}")
    @Loggable(value = "updateVehicle")
    public ResponseEntity<GenericResponse<Vehicle>> update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Loggable(value = "deleteVehicle")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.delete(id));
    }

    @GetMapping("/customer/{customerId}")
    @Loggable(value = "getVehiclesByCustomer")
    public ResponseEntity<GenericResponse<List<Vehicle>>> findByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.findByCustomerId(customerId));
    }
}
