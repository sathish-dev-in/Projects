package com.autonest.controller;

import com.autonest.annotation.Loggable;
import com.autonest.dto.request.CustomerRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Customer;
import com.autonest.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer REST controller.
 * Uses @Loggable for AOP-based logging and @Validated for method-level validation.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Validated
@Loggable
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Loggable(value = "createCustomer", logArgs = true)
    public ResponseEntity<GenericResponse<Customer>> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @GetMapping("/{id}")
    @Loggable(value = "getCustomer")
    public ResponseEntity<GenericResponse<Customer>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    @Loggable(value = "getAllCustomers", logArgs = false)
    public ResponseEntity<GenericResponse<List<Customer>>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PutMapping("/{id}")
    @Loggable(value = "updateCustomer")
    public ResponseEntity<GenericResponse<Customer>> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Loggable(value = "deleteCustomer")
    public ResponseEntity<GenericResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.delete(id));
    }

    @GetMapping("/search")
    @Loggable(value = "searchCustomers")
    public ResponseEntity<GenericResponse<List<Customer>>> search(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchByName(name));
    }
}
