package com.autonest.repository.postgres;

import com.autonest.entity.postgres.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNumber(String partNumber);

    boolean existsByPartNumber(String partNumber);

    List<Part> findByCategory(String category);

    @Query("SELECT p FROM Part p WHERE p.stockQuantity <= p.minimumStockLevel")
    List<Part> findLowStockParts();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.stockQuantity <= p.minimumStockLevel")
    long countLowStockParts();

    List<Part> findByActiveTrue();
}
