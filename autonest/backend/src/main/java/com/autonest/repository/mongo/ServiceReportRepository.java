package com.autonest.repository.mongo;

import com.autonest.entity.mongo.ServiceReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceReportRepository extends MongoRepository<ServiceReport, String> {

    Optional<ServiceReport> findByServiceOrderId(Long serviceOrderId);

    List<ServiceReport> findByVehicleId(Long vehicleId);

    List<ServiceReport> findByCustomerId(Long customerId);

    List<ServiceReport> findByMechanicId(Long mechanicId);
}
