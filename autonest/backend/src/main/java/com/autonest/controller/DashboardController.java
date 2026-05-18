package com.autonest.controller;

import com.autonest.annotation.Loggable;
import com.autonest.dto.response.DashboardStatsDTO;
import com.autonest.dto.response.GenericResponse;
import com.autonest.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Loggable
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Loggable(value = "getDashboardStats", logArgs = false)
    public ResponseEntity<GenericResponse<DashboardStatsDTO>> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
