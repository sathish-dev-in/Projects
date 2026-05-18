package com.autonest.service;

import com.autonest.dto.request.VehicleRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Vehicle;

import java.util.List;

public interface VehicleService extends BaseService<Vehicle, VehicleRequest, Long> {

    GenericResponse<List<Vehicle>> findByCustomerId(Long customerId);
}
