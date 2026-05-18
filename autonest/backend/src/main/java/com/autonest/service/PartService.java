package com.autonest.service;

import com.autonest.dto.request.PartRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Part;

import java.util.List;

public interface PartService extends BaseService<Part, PartRequest, Long> {

    GenericResponse<List<Part>> findLowStockParts();

    GenericResponse<Part> adjustStock(Long id, int quantity, String operation);
}
