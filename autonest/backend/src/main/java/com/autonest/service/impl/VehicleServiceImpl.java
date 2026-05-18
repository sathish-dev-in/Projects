package com.autonest.service.impl;

import com.autonest.dto.request.VehicleRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Customer;
import com.autonest.entity.postgres.Vehicle;
import com.autonest.exception.BusinessException;
import com.autonest.exception.ResourceNotFoundException;
import com.autonest.repository.postgres.CustomerRepository;
import com.autonest.repository.postgres.VehicleRepository;
import com.autonest.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Vehicle service implementation.
 * Demonstrates Streams, Lambdas, and Method References throughout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final ActivityLogService activityLogService;

    @Override
    public GenericResponse<Vehicle> create(VehicleRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.customerId()));

        if (vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new BusinessException("Vehicle with license plate '" + request.licensePlate() + "' already registered");
        }

        Vehicle vehicle = Vehicle.builder()
                .customer(customer)
                .make(request.make())
                .model(request.model())
                .year(request.year())
                .licensePlate(request.licensePlate())
                .vin(request.vin())
                .vehicleType(request.vehicleType())
                .color(request.color())
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);

        activityLogService.logAsync("Vehicle", saved.getId(), "CREATE",
                "Vehicle registered: " + saved.getDisplayName() + " for customer " + customer.getFullName());

        return GenericResponse.success(generateMessage("Vehicle", "registered"), saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<Vehicle> findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        return GenericResponse.success(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Vehicle>> findAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll()
                .stream()
                .filter(Vehicle::isActive)
                .toList();
        return GenericResponse.success(buildListMessage("Vehicle", vehicles.size()), vehicles);
    }

    @Override
    public GenericResponse<Vehicle> update(Long id, VehicleRequest request) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        if (!existing.getLicensePlate().equals(request.licensePlate())
                && vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new BusinessException("License plate '" + request.licensePlate() + "' is already registered");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.customerId()));

        existing.setCustomer(customer);
        existing.setMake(request.make());
        existing.setModel(request.model());
        existing.setYear(request.year());
        existing.setLicensePlate(request.licensePlate());
        existing.setVin(request.vin());
        existing.setVehicleType(request.vehicleType());
        existing.setColor(request.color());

        Vehicle updated = vehicleRepository.save(existing);

        activityLogService.logAsync("Vehicle", updated.getId(), "UPDATE",
                "Vehicle updated: " + updated.getDisplayName());

        return GenericResponse.success(generateMessage("Vehicle", "updated"), updated);
    }

    @Override
    public GenericResponse<Void> delete(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));

        vehicle.setActive(false);
        vehicleRepository.save(vehicle);

        activityLogService.logAsync("Vehicle", id, "DELETE",
                "Vehicle soft-deleted: " + vehicle.getDisplayName());

        return GenericResponse.successMessage(generateMessage("Vehicle", "deleted"));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Vehicle>> findByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", customerId);
        }
        List<Vehicle> vehicles = vehicleRepository.findByCustomerId(customerId)
                .stream()
                .filter(Vehicle::isActive)
                .toList();
        return GenericResponse.success("Vehicles for customer " + customerId, vehicles);
    }
}
