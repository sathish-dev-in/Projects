package com.autonest.repository.postgres;

import com.autonest.entity.postgres.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    Optional<Mechanic> findByEmail(String email);

    Optional<Mechanic> findByEmployeeId(String employeeId);

    List<Mechanic> findByActiveTrue();

    long countByActiveTrue();
}
