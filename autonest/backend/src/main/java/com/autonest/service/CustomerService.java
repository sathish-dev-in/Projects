package com.autonest.service;

import com.autonest.dto.request.CustomerRequest;
import com.autonest.dto.response.GenericResponse;
import com.autonest.entity.postgres.Customer;

import java.util.List;

public interface CustomerService extends BaseService<Customer, CustomerRequest, Long> {

    GenericResponse<List<Customer>> searchByName(String name);

    GenericResponse<Customer> findByEmail(String email);
}
