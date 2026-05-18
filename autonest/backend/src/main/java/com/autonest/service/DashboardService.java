package com.autonest.service;

import com.autonest.dto.response.DashboardStatsDTO;
import com.autonest.dto.response.GenericResponse;

public interface DashboardService {

    GenericResponse<DashboardStatsDTO> getStats();
}
