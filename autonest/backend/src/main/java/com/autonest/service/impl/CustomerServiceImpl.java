package com.autonest.service.impl;

import com.autonest.dto.request.CustomerRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Customer;
import com.autonest.exception.BusinessException;
import com.autonest.exception.ResourceNotFoundException;
import com.autonest.functional.EntityMapper;
import com.autonest.repository.postgres.CustomerRepository;
import com.autonest.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Customer service implementation.
 * Demonstrates: Streams, Lambdas, Method References, Optional, EntityMapper functional interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ActivityLogService activityLogService;

    // EntityMapper functional interface usage
    private final EntityMapper<CustomerRequest, Customer> requestToEntity = request ->
            Customer.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phone(request.phone())
                    .address(request.address())
                    .city(request.city())
                    .build();

    @Override
    public GenericResponse<Customer> create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new BusinessException("Customer with email '" + request.email() + "' already exists");
        }

        Customer customer = requestToEntity.map(request);
        Customer saved = customerRepository.save(customer);

        activityLogService.logAsync("Customer", saved.getId(), "CREATE",
                "New customer created: " + saved.getFullName());

        return GenericResponse.success(generateMessage("Customer", "created"), saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<Customer> findById(Long id) {
        // Optional usage for null-safe retrieval
        Customer customer = Optional.ofNullable(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id)))
                .get();

        return GenericResponse.success(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Customer>> findAll() {
        // Stream + method reference
        List<Customer> customers = customerRepository.findAll()
                .stream()
                .filter(Customer::isActive)
                .toList();

        return GenericResponse.success(buildListMessage("Customer", customers.size()), customers);
    }

    @Override
    public GenericResponse<Customer> update(Long id, CustomerRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        // Check email uniqueness only if email changed
        if (!existing.getEmail().equals(request.email()) && customerRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email '" + request.email() + "' is already in use by another customer");
        }

        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());
        existing.setEmail(request.email());
        existing.setPhone(request.phone());
        existing.setAddress(request.address());
        existing.setCity(request.city());

        Customer updated = customerRepository.save(existing);

        activityLogService.logAsync("Customer", updated.getId(), "UPDATE",
                "Customer updated: " + updated.getFullName());

        return GenericResponse.success(generateMessage("Customer", "updated"), updated);
    }

    @Override
    public GenericResponse<Void> delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        customer.setActive(false);
        customerRepository.save(customer);

        activityLogService.logAsync("Customer", id, "DELETE",
                "Customer soft-deleted: " + customer.getFullName());

        return GenericResponse.successMessage(generateMessage("Customer", "deleted"));
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<List<Customer>> searchByName(String name) {
        List<Customer> results = customerRepository.searchByName(name);
        return GenericResponse.success("Search results for: " + name, results);
    }

    @Override
    @Transactional(readOnly = true)
    public GenericResponse<Customer> findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
        return GenericResponse.success(customer);
    }
}
