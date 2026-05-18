package com.autonest.dto.request;

import com.autonest.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Immutable record for service order creation requests.
 */
public record ServiceOrderRequest(

        @NotNull(message = "Vehicle ID is required")
        Long vehicleId,

        Long mechanicId,

        @NotNull(message = "Service type is required")
        ServiceType serviceType,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Estimated cost is required")
        @Positive(message = "Estimated cost must be positive")
        BigDecimal estimatedCost,

        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        String notes
) {
}
