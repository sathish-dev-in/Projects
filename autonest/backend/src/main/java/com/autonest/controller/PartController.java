package com.autonest.controller;

import com.autonest.annotation.Loggable;
import com.autonest.dto.request.PartRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Part;
import com.autonest.service.PartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@Validated
@Loggable
public class PartController {

    private final PartService partService;

    @PostMapping
    @Loggable(value = "createPart", logArgs = true)
    public ResponseEntity<GenericResponse<Part>> create(@Valid @RequestBody PartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partService.create(request));
    }

    @GetMapping("/{id}")
    @Loggable(value = "getPart")
    public ResponseEntity<GenericResponse<Part>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.findById(id));
    }

    @GetMapping
    @Loggable(value = "getAllParts", logArgs = false)
    public ResponseEntity<GenericResponse<List<Part>>> findAll() {
        return ResponseEntity.ok(partService.findAll());
    }

    @PutMapping("/{id}")
    @Loggable(value = "updatePart")
    public ResponseEntity<GenericResponse<Part>> update(
            @PathVariable Long id,
            @Valid @RequestBody PartRequest request) {
        return ResponseEntity.ok(partService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Loggable(value = "deletePart")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(partService.delete(id));
    }

    @GetMapping("/low-stock")
    @Loggable(value = "getLowStockParts", logArgs = false)
    public ResponseEntity<GenericResponse<List<Part>>> findLowStock() {
        return ResponseEntity.ok(partService.findLowStockParts());
    }

    @PatchMapping("/{id}/stock")
    @Loggable(value = "adjustStock")
    public ResponseEntity<GenericResponse<Part>> adjustStock(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "ADD") String operation) {
        return ResponseEntity.ok(partService.adjustStock(id, quantity, operation));
    }
}
