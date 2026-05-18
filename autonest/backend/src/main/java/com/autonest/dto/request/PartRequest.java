package com.autonest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Immutable record for part/inventory item creation and update requests.
 */
public record PartRequest(

        @NotBlank(message = "Part name is required")
        @Size(max = 100, message = "Part name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Part number is required")
        @Size(max = 50, message = "Part number cannot exceed 50 characters")
        String partNumber,

        @Size(max = 200, message = "Description cannot exceed 200 characters")
        String description,

        @NotNull(message = "Unit price is required")
        @Positive(message = "Unit price must be positive")
        BigDecimal unitPrice,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        Integer stockQuantity,

        @NotNull(message = "Minimum stock level is required")
        @Min(value = 0, message = "Minimum stock level cannot be negative")
        Integer minimumStockLevel,

        @Size(max = 50, message = "Supplier cannot exceed 50 characters")
        String supplier,

        @Size(max = 50, message = "Category cannot exceed 50 characters")
        String category
) {
}
