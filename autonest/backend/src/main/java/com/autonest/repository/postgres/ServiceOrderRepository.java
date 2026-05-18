package com.autonest.repository.postgres;

import com.autonest.entity.postgres.ServiceOrder;
import com.autonest.enums.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    List<ServiceOrder> findByStatus(ServiceStatus status);

    List<ServiceOrder> findByVehicleId(Long vehicleId);

    List<ServiceOrder> findByMechanicId(Long mechanicId);

    long countByStatus(ServiceStatus status);

    @Query("SELECT COALESCE(SUM(s.actualCost), 0) FROM ServiceOrder s WHERE s.status = 'COMPLETED'")
    BigDecimal sumRevenueFromCompleted();

    @Query("SELECT s FROM ServiceOrder s WHERE s.status IN ('PENDING', 'IN_PROGRESS') ORDER BY s.createdAt DESC")
    List<ServiceOrder> findActiveOrders();
}
