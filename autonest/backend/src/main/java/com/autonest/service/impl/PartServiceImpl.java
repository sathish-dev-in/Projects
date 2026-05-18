package com.autonest.service.impl;

import com.autonest.dto.request.PartRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Part;
import com.autonest.exception.BusinessException;
import com.autonest.exception.ResourceNotFoundException;
import com.autonest.repository.postgres.PartRepository;
import com.autonest.service.PartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Part/Inventory service implementation.
 * Demonstrates Streams, Lambdas, Optional, and functional EntityMapper.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final ActivityLogService activityLogService;

    @Override
    public GenericResponse<Part> create(PartRequest request) {
        if (partRepository.existsByPartNumber(request.partNumber())) {
            throw new BusinessException("Part with number '" + request.partNumber() + "' already exists");
        }

        Part part = Part.builder()
                .name(request.name())
                .partNumber(request.partNumber())
                .description(request.description())
                .unitPrice(request.unitPrice())
                .stockQuantity(request.stockQuantity())
                .minimumStockLevel(request.minimumStockLevel())
                .supplier(request.supplier())
                .category(request.category())
                .build();

        Part saved = partRepository.save(part);

        activityLogService.logAsync("Part", saved.getId(), "CREATE",
                "New part added: " + saved.getName() + " [" + saved.getPartNumber() + "]");

        return GenericResponse.success(generateMessage("Part", "created"), saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<Part> findById(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", id));
        return GenericResponse.success(part);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Part>> findAll() {
        List<Part> parts = partRepository.findAll()
                .stream()
                .filter(Part::isActive)
                .toList();
        return GenericResponse.success(buildListMessage("Part", parts.size()), parts);
    }

    @Override
    public GenericResponse<Part> update(Long id, PartRequest request) {
        Part existing = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", id));

        if (!existing.getPartNumber().equals(request.partNumber())
                && partRepository.existsByPartNumber(request.partNumber())) {
            throw new BusinessException("Part number '" + request.partNumber() + "' is already in use");
        }

        existing.setName(request.name());
        existing.setPartNumber(request.partNumber());
        existing.setDescription(request.description());
        existing.setUnitPrice(request.unitPrice());
        existing.setStockQuantity(request.stockQuantity());
        existing.setMinimumStockLevel(request.minimumStockLevel());
        existing.setSupplier(request.supplier());
        existing.setCategory(request.category());

        Part updated = partRepository.save(existing);

        activityLogService.logAsync("Part", updated.getId(), "UPDATE",
                "Part updated: " + updated.getName());

        return GenericResponse.success(generateMessage("Part", "updated"), updated);
    }

    @Override
    public GenericResponse<Void> delete(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", id));

        part.setActive(false);
        partRepository.save(part);

        activityLogService.logAsync("Part", id, "DELETE",
                "Part soft-deleted: " + part.getName());

        return GenericResponse.successMessage(generateMessage("Part", "deleted"));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Part>> findLowStockParts() {
        List<Part> lowStockParts = partRepository.findLowStockParts();
        return GenericResponse.success("Low stock parts", lowStockParts);
    }

    @Override
    public GenericResponse<Part> adjustStock(Long id, int quantity, String operation) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", id));

        int newQuantity = switch (operation.toUpperCase()) {
            case "ADD" -> part.getStockQuantity() + quantity;
            case "SUBTRACT" -> {
                if (part.getStockQuantity() < quantity) {
                    throw BusinessException.insufficientStock(part.getName(), quantity, part.getStockQuantity());
                }
                yield part.getStockQuantity() - quantity;
            }
            case "SET" -> quantity;
            default -> throw new BusinessException("Invalid stock operation: " + operation + ". Use ADD, SUBTRACT, or SET");
        };

        part.setStockQuantity(newQuantity);
        Part updated = partRepository.save(part);

        activityLogService.logAsync("Part", updated.getId(), "STOCK_ADJUST",
                "Stock adjusted for " + part.getName() + ": " + operation + " " + quantity + " -> new qty: " + newQuantity);

        return GenericResponse.success("Stock adjusted successfully", updated);
    }
}
