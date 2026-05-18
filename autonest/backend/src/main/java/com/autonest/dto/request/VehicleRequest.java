package com.autonest.dto.request;

import com.autonest.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Immutable record for vehicle creation/update requests.
 */
public record VehicleRequest(

        @NotNull(message = "Customer ID is required")
        Long customerId,

        @NotBlank(message = "Make is required")
        @Size(max = 50, message = "Make cannot exceed 50 characters")
        String make,

        @NotBlank(message = "Model is required")
        @Size(max = 50, message = "Model cannot exceed 50 characters")
        String model,

        @NotNull(message = "Year is required")
        @Positive(message = "Year must be a positive number")
        Integer year,

        @NotBlank(message = "License plate is required")
        @Size(max = 20, message = "License plate cannot exceed 20 characters")
        String licensePlate,

        @Size(max = 17, message = "VIN cannot exceed 17 characters")
        String vin,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType,

        String color
) {
}
