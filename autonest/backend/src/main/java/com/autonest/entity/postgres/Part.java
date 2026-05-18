package com.autonest.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Part entity — an inventory item used in service orders.
 */
@Entity
@Table(name = "parts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Part extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "part_number", nullable = false, unique = true, length = 50)
    private String partNumber;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "minimum_stock_level", nullable = false)
    @Builder.Default
    private Integer minimumStockLevel = 5;

    @Column(name = "supplier", length = 50)
    private String supplier;

    @Column(name = "category", length = 50)
    private String category;

    public boolean isLowStock() {
        return stockQuantity <= minimumStockLevel;
    }

    public boolean isOutOfStock() {
        return stockQuantity == 0;
    }
}
