package com.autonest.service;

import com.autonest.dto.request.ServiceOrderRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.ServiceOrder;
import com.autonest.enums.ServiceStatus;

import java.util.List;

public interface ServiceOrderService extends BaseService<ServiceOrder, ServiceOrderRequest, Long> {

    GenericResponse<ServiceOrder> updateStatus(Long id, ServiceStatus newStatus);

    GenericResponse<List<ServiceOrder>> findByStatus(ServiceStatus status);

    GenericResponse<List<ServiceOrder>> findByVehicleId(Long vehicleId);

    GenericResponse<List<ServiceOrder>> findActiveOrders();
}
