package com.autonest.entity.postgres;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Mechanic entity — a garage employee who handles service orders.
 */
@Entity
@Table(name = "mechanics")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "serviceOrders")
public class Mechanic extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "employee_id", unique = true, length = 20)
    private String employeeId;

    @OneToMany(mappedBy = "mechanic", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ServiceOrder> serviceOrders = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
